import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadSocketHandler extends Thread {
  
  private DataInputStream dis;
  private DataOutputStream dos;
  private Socket s;
  private String fileName;

  // Create a ThreadSocketHandler class to handle Multithread Server Connections
  // The class is a subclass of Thread class and we will override the run method and replace it with whatever we need

  public ThreadSocketHandler(Socket s, DataInputStream dis, DataOutputStream dos, String fileName){
    this.s = s;
    this.dis = dis;
    this.dos = dos;
    this.fileName = fileName;
  }

  // All
  @Override
  public void run(){
    // Handle Socket Communication here
    // Identical setup as a single server connection

    while(true){
      try {
        String fromClient = dis.readUTF();
        if(fromClient.equalsIgnoreCase("exit")){
          break;
        }
        if (fromClient.equalsIgnoreCase("get-cookie")){
          CookieFile cookie = new CookieFile();
          String cookieText = cookie.getRandomCookieFromFile(fromClient);
          dos.writeUTF("cookie-text");
          dos.writeUTF(cookieText);
          dos.flush();
        } else{
          System.out.println("Invalid Command");
          dos.writeUTF("From Server: Invalid Command");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try{
      s.close();
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
