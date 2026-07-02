package demo.controller;

import demo.entity.User;
import demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name="Users",description = "User Profile Management API'S")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;
//these methods will work when we will call endpoints with the bearer  token
    @GetMapping("/profile")
    @Operation(summary = "Get the logged-in user's profile")

    public ResponseEntity<User> getProfile(Authentication authentication) {

        User user =
                userService.findByUserName(authentication.getName());

        return ResponseEntity.ok(user);
    }
    @Operation(summary = "Update the logged-in user's profile")
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            Authentication authentication,
            @Valid @RequestBody User updatedUser) {

        User user =
                userService.updateUser(
                        authentication.getName(),
                        updatedUser);

        return ResponseEntity.ok(user);
    }
    @Operation(summary = "Delete the logged-in user's profile")
    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile(
            Authentication authentication) {

        userService.deleteUser(authentication.getName());

        return ResponseEntity.ok("User deleted successfully.");
    }
}