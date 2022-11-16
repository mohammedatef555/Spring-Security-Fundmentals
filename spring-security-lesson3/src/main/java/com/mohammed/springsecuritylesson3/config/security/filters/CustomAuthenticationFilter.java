package com.mohammed.springsecuritylesson3.config.security.filters;

import com.mohammed.springsecuritylesson3.config.security.manager.CustomAuthenticationManager;
import com.mohammed.springsecuritylesson3.config.security.authentication.CustomAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CustomAuthenticationManager manager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 1. create an authentication object which is not yet authenticated
        String key = String.valueOf(request.getHeader("key"));
        CustomAuthentication ca = new CustomAuthentication(false, key);

        // 2. delegate the authentication object to the manager
        // 3. get back the authentication from the manager
        Authentication authentication = manager.authenticate(ca);
        // 4. if the object is authenticated then send request to the next filter in the chain
        if (authentication.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);// only when authentication worked
        }
    }
}
