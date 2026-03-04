@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ApiKeyFilter apiKeyFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Allow these
                .anyRequest().authenticated()
            )
            // Add our API Key filter BEFORE the standard security filter
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}