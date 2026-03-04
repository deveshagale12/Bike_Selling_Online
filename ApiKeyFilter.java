@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.security.api-key}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestApiKey = request.getHeader("X-API-KEY");
        System.out.println("Incoming API Key: " + requestApiKey); // DEBUG LINE
        System.out.println("Expected API Key: " + validApiKey);   // DEBUG LINE

        if (validApiKey != null && validApiKey.equals(requestApiKey)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(401);
            response.getWriter().write("401 Unauthorized: Invalid API Key");
        }
    }
}