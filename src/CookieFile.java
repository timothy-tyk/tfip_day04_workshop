import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CookieFile {
  private String fileName;
  public CookieFile(){
    
  }
  // public CookieFile(String fileName){
  //   this.fileName = fileName;
  // }

  public String getRandomCookieFromFile(String fileName){
    List<String> lines;
    try {
      lines = Files.readAllLines(Paths.get(fileName));
      Random random = new Random();
      int randomInt = random.nextInt(lines.size()-1);
      return lines.get(randomInt);
    } catch (IOException e) {
      return "Error: No Cookie Found!";
    }
  }

  // Below code can replace Files.readAllLines() on line #20
  public List<String> readFile(){
  List <String> cookieLines = new LinkedList<String>();

  try {
    BufferedReader bfr = new BufferedReader(new FileReader(this.fileName));
    String line;

    while((line = bfr.readLine())!=null){
      line = line.trim();
      cookieLines.add(line);
    }
    bfr.close();
  }
  catch(FileNotFoundException e){
    e.printStackTrace();
  } 
  catch (IOException err) {
    err.printStackTrace();
  } 
  
  return cookieLines;
}
}
