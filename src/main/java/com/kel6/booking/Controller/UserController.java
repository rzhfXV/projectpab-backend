package com.kel6.booking.Controller;

import com.kel6.booking.DTO.response.ApiResponse;
import com.kel6.booking.Model.User;
import com.kel6.booking.Repository.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/profile/update-name")
    public ResponseEntity<ApiResponse<String>> updateName(
            @RequestBody NameRequest req, 
            @AuthenticationPrincipal User user) {
        
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Nama tidak boleh kosong"));
        }

        user.setName(req.getName().trim());
        userRepository.save(user);
        
        return ResponseEntity.ok(ApiResponse.success("Nama profil berhasil diperbarui", user.getName()));
    }

    @PostMapping(value = "/profile/update-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateAvatar(
            @RequestParam("file") MultipartFile file, 
            @AuthenticationPrincipal User user) throws IOException {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File gambar tidak ditemukan"));
        }

        String filename = "avatar_" + user.getId() + "_" + System.currentTimeMillis() + ".jpg";
        
        user.setProfilePicture(filename); 
        userRepository.save(user);
        
        return ResponseEntity.ok(ApiResponse.success("Foto profil berhasil diperbarui", filename));
    }

    @PutMapping("/profile/update-account")
    public ResponseEntity<ApiResponse<String>> updateAccount(
            @RequestBody AccountRequest req, 
            @AuthenticationPrincipal User user) {
        
        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email tidak boleh kosong"));
        }

        user.setEmail(req.getEmail().trim());
        
        if (req.getNewPassword() != null && !req.getNewPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(req.getNewPassword().trim()));
        }
        
        userRepository.save(user);
        
        return ResponseEntity.ok(ApiResponse.success("Data akun berhasil diperbarui", user.getEmail()));
    }
}

@Data @NoArgsConstructor @AllArgsConstructor class NameRequest { private String name; }
@Data @NoArgsConstructor @AllArgsConstructor class AccountRequest { private String email; private String newPassword; }