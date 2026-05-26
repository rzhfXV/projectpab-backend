package com.kel6.booking.Controller;

import com.kel6.booking.DTO.request.LoginRequest;
import com.kel6.booking.DTO.request.RegisterRequest;
import com.kel6.booking.DTO.response.ApiResponse;
import com.kel6.booking.DTO.response.AuthResponse;
import com.kel6.booking.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Body: { "name": "Budi", "email": "budi@mail.com",
     *         "password": "secret123", "phone": "081234567890" }
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse data = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registrasi berhasil", data));
    }

    /**
     * POST /api/auth/login
     * Body: { "email": "budi@mail.com", "password": "secret123" }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse data = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login berhasil", data));
    }

    /**
     * GET /api/auth/me
     * Header: Authorization: Bearer <token>
     * Kembalikan data user yang sedang login (berguna untuk cek token masih valid)
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse.UserInfo>> getCurrentUser(
            @RequestAttribute(required = false) Object principal) {

        // Ambil dari SecurityContext lewat Spring — inject User langsung
        org.springframework.security.core.Authentication auth =
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication();

        com.kel6.booking.Model.User user = (com.kel6.booking.Model.User) auth.getPrincipal();

        AuthResponse.UserInfo info = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(ApiResponse.success(info));
    }
}
