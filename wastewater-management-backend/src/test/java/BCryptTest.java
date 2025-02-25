import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "020714";  // 这里是你要加密的密码
        String encodedPassword = encoder.encode(rawPassword);  // 加密后的密码
        System.out.println("BCrypt Encrypted Password: " + encodedPassword);
    }
}
