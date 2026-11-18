package ru.diplom.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.diplom.dto.request.*;
import ru.diplom.dto.response.AdResponse;
import ru.diplom.dto.response.AdsResponse;
import ru.diplom.dto.response.UserResponse;
import ru.diplom.entity.User;
import ru.diplom.service.AdService;
import ru.diplom.service.ImageService;
import ru.diplom.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
