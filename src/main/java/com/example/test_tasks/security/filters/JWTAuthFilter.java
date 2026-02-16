package com.example.test_tasks.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test_tasks.security.services.CustomUserDetailsService;
import com.example.test_tasks.security.services.JWTManagerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTAuthFilter extends OncePerRequestFilter {

    JWTManagerService jwtManager;
    CustomUserDetailsService details;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain){

        String header = request.getHeader("Authorization");

        if(header == null || (!header.startsWith("Bearer"))){
            filterChain.doFilter(request,response);
            return;
        }

        header = header.substring(7);

        DecodedJWT jwtBody = jwtManager.plainJwtDecoder(header);

        UserDetails user = details.loadUserByUsername(jwtBody.getSubject());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request,response);
    }
}
