
package ru.diplom.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.diplom.dto.request.NewPasswordRequest;
import ru.diplom.dto.request.RegisterRequest;
import ru.diplom.dto.request.UpdateUserRequest;
import ru.diplom.dto.response.UserResponse;
import ru.diplom.entity.User;
import ru.diplom.entity.UserRole;
import ru.diplom.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole() != null ? UserRole.valueOf(request.getRole()) : UserRole.USER);

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse getUser() {
        User currentUser = getCurrentUser();
        return toResponse(currentUser);
    }

    public UserResponse updateUser(UpdateUserRequest request) {
        User currentUser = getCurrentUser();

        if (request.getFirstName() != null) {
            currentUser.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            currentUser.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            currentUser.setPhone(request.getPhone());
        }

        User saved = userRepository.save(currentUser);
        return toResponse(saved);
    }

    public void updateUserImage(String imagePath) {
        User currentUser = getCurrentUser();
        currentUser.setImage(imagePath);
        userRepository.save(currentUser);
    }

    public void setPassword(NewPasswordRequest request) {
        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().name());
        response.setImage(user.getImage());
        return response;
    }
}
