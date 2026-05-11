import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class CheckPasswords2 {
  public static void main(String[] args) {
    BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    String[][] hashes = {
      {"admin_old", "$2a$10$q.giKQUjTQZaXMCXT33FteZdtCstfc6N2COJgbeCPj5kvhjKdzPfq"},
      {"staff_old", "$2a$10$psR.78ZKovvZeLpO01.iUeg9yJUfCTM8tggq0V1SpADelCz.PEgkG"},
      {"admin_new", "$2a$10$uwq8TV6jQp9qLBi9LWO1ReITVk.VNgTHbVXeeheD9CPnE9C.9J/CG"},
      {"staff_new", "$2a$10$hWcLinFMnlTYzPVe471G5.ohKV8XikIDP086mfa5HjiiYqJYn8nVW"}
    };
    String[] candidates = {
      "admin","admin1","admin12","admin123","admin1234","admin12345","admin2024","admin2025","admin2026",
      "administrator","systemadministrator","system","system123","support","support123","staff","staff1","staff12","staff123","staff1234","staff2024","staff2025","staff2026",
      "password","password1","123456","1234567","12345678","123456789","root","root123","unab","unab123","veterinary","veterinary123",
      "admin@veterinary.local","staff@veterinary.local","System Administrator","Support Staff",
      "Admin123","Staff123","Admin1234","Staff1234","Password123","admin123.","staff123.","Admin2026","Staff2026"
    };
    for (String[] pair : hashes) {
      String user = pair[0];
      String hash = pair[1];
      System.out.println("USER=" + user);
      boolean found = false;
      for (String c : candidates) {
        if (enc.matches(c, hash)) {
          System.out.println("MATCH=" + c);
          found = true;
        }
      }
      if (!found) {
        System.out.println("NO_MATCH_IN_CANDIDATES");
      }
    }
  }
}
