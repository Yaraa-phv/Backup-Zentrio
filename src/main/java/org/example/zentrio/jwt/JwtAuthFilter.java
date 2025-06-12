package org.example.zentrio.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.service.AppUserService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
//            email = jwtService.extractEmail(token);
            // new updated
//            if(token.chars().filter(ch -> ch == '=').count() != 2) {
//                filterChain.doFilter(request, response);
//                return;
//            }
            try {
                email = jwtService.extractEmail(token);
            } catch (Exception e) {
                logger.warn("Failed to extract email from JWT: {}");
                filterChain.doFilter(request, response);
                return;
            }
            // end new updated
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            AppUser appUser = (AppUser) appUserService.loadUserByUsername(email);
            if (appUser != null) {
                if (jwtService.validateToken(token, appUser)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(appUser, null, appUser.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
