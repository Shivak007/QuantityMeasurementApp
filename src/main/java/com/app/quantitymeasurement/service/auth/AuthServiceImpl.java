package com.app.quantitymeasurement.service.auth;

import com.app.quantitymeasurement.config.google.GoogleTokenVerifier;
import com.app.quantitymeasurement.config.google.GoogleUserInfo;
import com.app.quantitymeasurement.config.jwt.JwtUtil;
import com.app.quantitymeasurement.model.auth.*;
import com.app.quantitymeasurement.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .provider(User.AuthProvider.LOCAL)
                .build();
        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());
        return buildResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (user.getProvider() != User.AuthProvider.LOCAL || user.getPassword() == null) {
            throw new BadCredentialsException("Please use Google Sign-In for this account");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        log.info("User logged in: {}", user.getEmail());
        return buildResponse(user);
    }

    @Override
    public AuthResponse googleLogin(GoogleLoginRequest request) {
        GoogleUserInfo info = googleTokenVerifier.verify(request.getIdToken());
        User user = userRepository.findByEmail(info.getEmail()).orElseGet(() -> {
            User newUser = User.builder()
                    .name(info.getName())
                    .email(info.getEmail())
                    .googleId(info.getSub())
                    .provider(User.AuthProvider.GOOGLE)
                    .build();
            log.info("New Google user auto-registered: {}", info.getEmail());
            return userRepository.save(newUser);
        });
        // Update googleId if missing (e.g. existing LOCAL user signs in with Google)
        if (user.getGoogleId() == null) {
            user.setGoogleId(info.getSub());
            user.setProvider(User.AuthProvider.GOOGLE);
            userRepository.save(user);
        }
        log.info("Google login success: {}", user.getEmail());
        return buildResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        return AuthResponse.builder()
                .accessToken(jwtUtil.generateToken(user.getEmail()))
                .tokenType("Bearer")
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
