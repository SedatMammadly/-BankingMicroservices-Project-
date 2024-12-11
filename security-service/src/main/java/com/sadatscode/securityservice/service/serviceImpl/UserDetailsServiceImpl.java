package com.sadatscode.securityservice.service.serviceImpl;

import com.sadatscode.securityservice.model.CustomUserDetails;
import com.sadatscode.securityservice.model.Users;
import com.sadatscode.securityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repository.findByEmail(username);
        return new CustomUserDetails(user);
    }
}
