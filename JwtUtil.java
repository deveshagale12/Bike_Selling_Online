@Component
public class JwtUtil {

    @Value("${app.security.jwt-secret}")
    private String secret;

    private Key getSigningKey() {
        // This converts your string to a byte array
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        
        // If the key is still too short, this will throw the error immediately 
        // upon startup rather than waiting for a login attempt.
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 Hour
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}