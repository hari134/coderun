package com.hari134.api_gateway.service.impl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import com.hari134.api_gateway.repository.EmailAuthRepository;
import com.hari134.api_gateway.entity.EmailAuth;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    public static class CustomUserDetails extends User {
        private Long userId;

        public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId) {
            super(username, password, authorities);
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EmailAuth emailAuth = emailAuthRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Long userId = emailAuth.getUser().getUserId();

        return new CustomUserDetails(emailAuth.getEmail(), emailAuth.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")), userId);
    }
}
