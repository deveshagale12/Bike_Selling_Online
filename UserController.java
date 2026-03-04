@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private JwtUtil jwtUtil;

    // Injecting the API Key directly from properties
    @Value("${app.security.api-key}")
    private String validApiKey;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestBody User user) {

        // 1. Manual API Key Check (to avoid 401 filter issues)
        if (apiKey == null || !apiKey.equals(validApiKey)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Invalid API Key"));
        }

        // 2. Email existence check
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(409).body(Map.of(
                "success", false,
                "message", "Email already registered!"
            ));
        }

        // 3. Save User (ID is random UUID)
        User savedUser = userRepository.save(user);

        // 4. Trigger Async Mail
        emailService.sendRegistrationMail(savedUser.getEmail(), savedUser.getFirstName());

        // 5. Return JSON Map
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "User created successfully");
        response.put("user_id", savedUser.getUserId());
        
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestBody Map<String, String> loginData) {

        // API Key Check
        if (apiKey == null || !apiKey.equals(validApiKey)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid API Key"));
        }

        String email = loginData.get("email");
        String password = loginData.get("password");

        User user = userRepository.findByEmail(email);
        
        if (user != null && user.getPassword().equals(password)) {
            String token = jwtUtil.generateToken(user.getEmail());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "token", token,
                "user_id", user.getUserId()
            ));
        }

        return ResponseEntity.status(401).body(Map.of(
            "success", false, 
            "message", "Invalid credentials"
        ));
    }

    @Scheduled(fixedRate = 60000)
    public void statusCheck() {
        System.out.println("System Heartbeat: " + LocalDateTime.now() + " | Thread: " + Thread.currentThread().getName());
    }
}