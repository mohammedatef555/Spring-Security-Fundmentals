package com.mohammed.Springsecuritylesson4.config.filter;

import com.mohammed.Springsecuritylesson4.config.authentication.CustomAuthentication;
import com.mohammed.Springsecuritylesson4.config.manager.CustomAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ApiKeyFiler extends OncePerRequestFilter {


    private final String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestKey = request.getHeader("x-api-key");

        if (requestKey == null || requestKey.equals("null")) {
            filterChain.doFilter(request, response);
        }

        CustomAuthentication authentication = new CustomAuthentication(requestKey);

        CustomAuthenticationManager manager = new CustomAuthenticationManager(key);

        try {
            Authentication auth = manager.authenticate(authentication);
            if (auth.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (AuthenticationException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }
}
