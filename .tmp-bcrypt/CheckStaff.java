import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
public class CheckStaff {
  public static void main(String[] args) {
    BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    String hash = "$2a$10$hWcLinFMnlTYzPVe471G5.ohKV8XikIDP086mfa5HjiiYqJYn8nVW";
    LinkedHashSet<String> candidates = new LinkedHashSet<>();
    String[] bases = {"staff","support","veterinary","unab","corhuila","password","root"};
    String[] suffixes = {"","1","12","123","1234","2024","2025","2026","@123","!"};
    for (String b : bases) {
      for (String s : suffixes) {
        candidates.add(b+s);
        candidates.add(Character.toUpperCase(b.charAt(0))+b.substring(1)+s);
      }
    }
    candidates.add("Support Staff");
    candidates.add("support staff");
    candidates.add("staff@veterinary.local");
    System.out.println("CANDIDATES="+candidates.size());
    for (String c : candidates) {
      if (enc.matches(c, hash)) { System.out.println("MATCH="+c); return; }
    }
    System.out.println("NO_MATCH_IN_GENERATED_LIST");
  }
}
