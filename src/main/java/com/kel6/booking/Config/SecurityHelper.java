package com.kel6.booking.Config;

import com.kel6.booking.Model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Helper untuk ambil data user yang sedang login.
 * Pakai di controller atau service dengan inject @Autowired / constructor injection.
 *
 * Contoh pemakaian di controller:
 *   User currentUser = securityHelper.getCurrentUser();
 *   Long userId = securityHelper.getCurrentUserId();
 */
@Component
public class SecurityHelper {

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Tidak ada user yang sedang login");
        }
        return (User) auth.getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUserRole() {
        return getCurrentUser().getRole().name();
    }

    public boolean isAdmin() {
        return getCurrentUser().getRole() == User.Role.ADMIN;
    }
}
