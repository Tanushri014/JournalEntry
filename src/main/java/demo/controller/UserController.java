package demo.controller;

import demo.dto.UserResponse;
import demo.entity.User;
import demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User Profile Management API'S")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get the logged-in user's profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {

        UserResponse user =
                userService.getUserProfile(authentication.getName());

        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update the logged-in user's profile")
    public ResponseEntity<UserResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody User updatedUser) {

        UserResponse user =
                userService.updateUser(
                        authentication.getName(),
                        updatedUser);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/profile")
    @Operation(summary = "Delete the logged-in user's profile")
    public ResponseEntity<String> deleteProfile(
            Authentication authentication) {

        userService.deleteUser(authentication.getName());

        return ResponseEntity.ok("User deleted successfully.");
    }
}