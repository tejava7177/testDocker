package com.cloud.proxyserver.controller;

import com.cloud.proxyserver.domain.User;
import com.cloud.proxyserver.dto.JwtRequest;
import com.cloud.proxyserver.dto.SignupRequest;
import com.cloud.proxyserver.repository.UserRepository;
import com.cloud.proxyserver.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공!");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("유효하지 않은 이메일");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok().body("Bearer " + token);
    }


    @RestController
    @RequestMapping("/api/user")
    public class UserController {

        @GetMapping("/me")
        public ResponseEntity<String> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
            return ResponseEntity.ok("현재 로그인한 사용자: " + userDetails.getUsername());
        }
    }
}