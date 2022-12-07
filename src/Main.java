public class Main {
  public static void main(String[] args) {
    CookieFile cookie = new CookieFile();
    System.out.println(cookie.getRandomCookieFromFile("src/cookie_file.txt"));
  }
}
