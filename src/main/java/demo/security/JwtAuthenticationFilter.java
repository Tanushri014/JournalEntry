package demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Read the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: If there is no Bearer token, continue the filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Remove "Bearer " prefix
        String jwt = authHeader.substring(7);

        try {

            // Step 4: Extract username from JWT
            String username = jwtService.extractUsername(jwt);

            // Step 5: Authenticate only if the SecurityContext is empty
            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // Step 6: Load user from MongoDB
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // Step 7: Validate the token
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Step 8: Attach request details
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // Step 9: Store authentication in SecurityContext
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception exception) {

            // Invalid or expired JWT.
            // Continue without authentication.
        }

        // Step 10: Continue to the next filter
        filterChain.doFilter(request, response);
    }
}