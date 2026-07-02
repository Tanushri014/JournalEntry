package demo.service;

import demo.dto.JwtResponse;
import demo.dto.LoginRequest;
import demo.dto.RegisterRequest;
import demo.entity.User;
import demo.exception.UserAlreadyExistsException;
import demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public void register(RegisterRequest request) {

        log.info("Registering user {}", request.getUserName());

        if (userService.existsByUserName(request.getUserName())) {
            log.warn("Registration failed. Username already exists: {}", request.getUserName());
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of("USER"));

        userService.saveUser(user);

        log.info("User {} registered successfully", request.getUserName());

        try {
            sendRegistrationSuccessMail(request.getUserName());
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}", request.getUserName(), e);
        }
    }

    public JwtResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUserName());

        String jwt = jwtService.generateToken(userDetails);

        log.info("User {} logged in successfully", request.getUserName());

        return new JwtResponse(jwt);
    }

    private void sendRegistrationSuccessMail(String toUserName) {

        emailService.sendEmail(
                toUserName,
                "Registration Successful",
                "Welcome! Your account has been created successfully."
        );
    }
}