package com.likelion.mooding.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/session")
    public ResponseEntity<Void> createSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        if (session.isNew()) {
            session.setAttribute("guestId", UUID.randomUUID());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }
}
