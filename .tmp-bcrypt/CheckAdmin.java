import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
public class CheckAdmin {
  public static void main(String[] args) {
    BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    String hash = "$2a$10$uwq8TV6jQp9qLBi9LWO1ReITVk.VNgTHbVXeeheD9CPnE9C.9J/CG";
    LinkedHashSet<String> candidates = new LinkedHashSet<>();
    String[] bases = {"admin","system","administrator","veterinary","unab","corhuila","password","root"};
    String[] suffixes = {"","1","12","123","1234","2024","2025","2026","@123","!"};
    for (String b : bases) {
      for (String s : suffixes) {
        candidates.add(b+s);
        candidates.add(Character.toUpperCase(b.charAt(0))+b.substring(1)+s);
      }
    }
    candidates.add("System Administrator");
    candidates.add("system administrator");
    candidates.add("admin@veterinary.local");
    System.out.println("CANDIDATES="+candidates.size());
    for (String c : candidates) {
      if (enc.matches(c, hash)) { System.out.println("MATCH="+c); return; }
    }
    System.out.println("NO_MATCH_IN_GENERATED_LIST");
  }
}
