import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketApp {

    // To run:
    // cd src/day04-workshop/src
    // javac SocketApp.java
    // For Server:
    // java SocketApp server 12345 cookie_file.txt
    // For Client:
    // java SocketApp client localhost 12345
  public static void main(String[] args) {

    // command line arguments are space separated (argnet)
    // Whatever is after the filename are argnets
    // These don't run using the play button, it requires input with the terminal
    
    String usage = """
        Incorrect inputs, please check the following usage:
            ================
            > Usage: Server
            <program> <'server'> <port> <filename>
            ================
            > Usage: Multithread Server
            <program> <'thread.server'> <port> <filename>
            ================
            > Usage: Client
            <program> <'client'> <host> <port>
            ================
        """;

    if (args.length!=3){
      System.out.println("Incorrect inputs. Please check the following usage.");
      System.out.println(usage);
      return;
    }
    String type = args[0];
    if(type.equalsIgnoreCase("server")){
      int PORT = Integer.parseInt(args[1]);
      String fileName = args[2];
      StartServer(PORT, fileName);
    }else if(type.equalsIgnoreCase("thread.server")){
      int PORT = Integer.parseInt(args[1]);
      String fileName = args[2];
      startMultiThreadServer(PORT, fileName);
    }else if(type.equalsIgnoreCase("client")){
      String hostName = args[1];
      int PORT = Integer.parseInt(args[2]);
      startClient(hostName, PORT);
      
    }else{
      System.out.println("Invalid Argnets!");
    }

  }
  public static void StartServer(int PORT, String fileName){
    ServerSocket server;
    try {
      server = new ServerSocket(PORT);
      Socket socket = server.accept();

      // IN
      InputStream is = socket.getInputStream();
      BufferedInputStream bis = new BufferedInputStream(is);
      DataInputStream dis = new DataInputStream(bis);

      //OUT
      OutputStream os = socket.getOutputStream();
      BufferedOutputStream bos = new BufferedOutputStream(os);
      DataOutputStream dos = new DataOutputStream(bos);

      while (true) {
        String fromClient = dis.readUTF();
        if(fromClient.equals("exit")){
          break;
        }
        System.out.println("LOG: msg from client : "+fromClient);
        if(fromClient.equalsIgnoreCase("get-cookie")){
          //Get random cookie from file
          CookieFile cookie = new CookieFile();
          String cookieText = cookie.getRandomCookieFromFile(fileName);
          //Send the random cookie text to the client
          dos.writeUTF("cookie-text");
          dos.writeUTF(cookieText);
          dos.flush();
        }
        else{
          System.out.println("Invalid command");
          dos.writeUTF("From server: Invalid Command");
          dos.flush();
        }
      }
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void startMultiThreadServer(int PORT, String fileName){
    
    try {
      ServerSocket server = new ServerSocket(PORT);
      Socket socket = server.accept();
       while (true) {
    
    //INPUT
    DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    //OUTPUT
    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    Thread tsh = new ThreadSocketHandler(socket, dis, dos, fileName);
    tsh.start();
    }
  
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void startClient(String hostname, int PORT){
    try{
      Socket socket = new Socket("localhost", PORT);

      //Input
      DataInputStream dis= new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      // Output
      DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

      // Scanner
      Scanner sc = new Scanner(System.in);
      boolean stop = false;

      while(!stop){
        String line = sc.nextLine();
        if(line.equalsIgnoreCase("exit")){
          dos.writeUTF("exit");
          dos.flush();
          stop = true;
          break;
      }
      if(line.equalsIgnoreCase("get-cookie")){
        // tell the server to get the cookie text
        dos.writeUTF("get-cookie");
        dos.flush();
        
        //Receiving cookie text from server
        String fromServer = dis.readUTF();
        if(fromServer.equalsIgnoreCase("cookie-text")){
          String cookieText = dis.readUTF();
          System.out.println(cookieText);
        }
      }else{
        System.out.println("Invalid Command: "+line);
      }
      }
      
    }catch(UnknownHostException e){
      e.printStackTrace();
    }catch(IOException e){
      e.printStackTrace();
    }
  }


}
