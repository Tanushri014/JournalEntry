package demo.controller;
import demo.dto.JwtResponse;
import demo.dto.LoginRequest;
import demo.dto.RegisterRequest;
import demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name="Authentication",description = "User registration and login API'S")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user using email and Password")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        authenticationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
@Operation(summary = "Authenticate registerd user and get jwt token")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request) {

        JwtResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(response);
    }
}