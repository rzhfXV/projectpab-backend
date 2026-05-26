package com.kel6.booking.Service;

import com.kel6.booking.Config.JwtUtil;
import com.kel6.booking.DTO.request.LoginRequest;
import com.kel6.booking.DTO.request.RegisterRequest;
import com.kel6.booking.DTO.response.AuthResponse;
import com.kel6.booking.Exception.BusinessException;
import com.kel6.booking.Model.User;
import com.kel6.booking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // ─── Register ─────────────────────────────────────────────────────
    public AuthResponse register(RegisterRequest request) {

        // Cek apakah email sudah dipakai
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email " + request.getEmail() + " sudah terdaftar");
        }

        // Buat user baru
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.USER)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);

        return AuthResponse.of(token, savedUser);
    }

    // ─── Login ────────────────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {

        // authenticationManager akan throw BadCredentialsException
        // kalau email/password salah — ditangkap GlobalExceptionHandler
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Kalau sampai sini berarti kredensial benar
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("User tidak ditemukan"));

        String token = jwtUtil.generateToken(user);

        return AuthResponse.of(token, user);
    }
}
