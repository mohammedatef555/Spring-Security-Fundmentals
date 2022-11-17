package com.mohammed.Springsecuritylesson4.config.manager;

import com.mohammed.Springsecuritylesson4.config.authentication.CustomAuthentication;
import com.mohammed.Springsecuritylesson4.config.provdiers.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider(key);

        if (provider.supports(authentication.getClass())) {
            return provider.authenticate(authentication);
        }

        return authentication;

    }
}
