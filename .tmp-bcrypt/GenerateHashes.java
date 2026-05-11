import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class GenerateHashes {
  public static void main(String[] args) {
    BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    System.out.println("admin1234=" + enc.encode("admin1234"));
    System.out.println("staff1234=" + enc.encode("staff1234"));
  }
}
