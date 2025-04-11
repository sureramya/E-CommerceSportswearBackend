package com.example.service;
 
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
import java.util.Collections;
import java.util.List;
 
 
@Service
public class UserService implements UserDetailsService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
 
    public User register(User user) {
        if (user.getRole() == null) {
            user.setRole("USER");  // Default role
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
 
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())) // Add role to authorities
        );
    }
}
 