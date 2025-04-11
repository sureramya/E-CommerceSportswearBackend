package com.example.controller;
 
import java.util.Collections;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.example.dto.AuthResponse;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.UserService;
 
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	  @Autowired
	    private UserRepository userRepository; 
	   @Autowired
	    private PasswordEncoder passwordEncoder;
 
    @Autowired
    private UserService userService;
 
    @Autowired
    private JwtUtil jwtUtil;
 
    @Autowired
    private AuthenticationManager authManager;
 
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        return ResponseEntity.ok(userService.register(user));
    }
 
 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        User user = userRepository.findByUsername(request.getUsername());
 
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
 
        // ✅ Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
 
        // ✅ Return token along with user info
        AuthResponse response = new AuthResponse(token, user.getUsername());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(null); // hide password
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedUser) {
        User user = userRepository.findByUsername(updatedUser.getUsername());
        if (user != null) {
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok("Profile updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

 
 
}