package demo.controller;
import demo.dto.JwtResponse;
import demo.dto.LoginRequest;
import demo.dto.RegisterRequest;
import demo.dto.VerifyOtpRequest;
import demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name="Authentication",description = "User registration and login API'S")
@CrossOrigin(origins = "http://localhost:5173",
allowCredentials = "true")
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
    @Operation(summary = "Authenticate registered user")
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request) {

        String jwt = authenticationService.login(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false)          // Change to true after HTTPS deployment
                .sameSite("None")        // Change to None after deployment
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful");
    }
@Operation(summary = "verify the otp")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyRegistration(
            @Valid @RequestBody VerifyOtpRequest request) {

        authenticationService.verifyRegistration(
                request.getUserEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok("Email veriied successfully");
    }
}