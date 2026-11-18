package ru.diplom.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.diplom.dto.request.LoginRequest;
import ru.diplom.dto.response.UserResponse;
import ru.diplom.service.UserService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // This endpoint is intentionally unsecured - Spring Security handles authentication
    @PostMapping
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        // Authentication is handled by Spring Security filter
        return ResponseEntity.ok().build();
    }
}
