package demo.service;

import demo.dto.JwtResponse;
import demo.dto.LoginRequest;
import demo.dto.RegisterRequest;
import demo.entity.PendingRegistration;
import demo.entity.User;
import demo.exception.InvalidOtpException;
import demo.exception.PendingRegistrationAlreadyExistsException;
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

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final PendingRegistrationService pendingRegistrationService;
    private final OTPService otpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public void register(RegisterRequest request) {
        log.info("Java LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("Java ZoneId: {}", java.time.ZoneId.systemDefault());
        log.info("Registering user {}", request.getUserEmail());

        if (userService.existsByUserEmail(request.getUserEmail())) {
            throw new UserAlreadyExistsException("User already exists.");
        }

        if (pendingRegistrationService.exists(request.getUserEmail())) {
            throw new PendingRegistrationAlreadyExistsException(
                    "A verification request is already pending. Please verify your email first."
            );
        }

        PendingRegistration pendingRegistration = new PendingRegistration();

        pendingRegistration.setUserEmail(request.getUserEmail());
        pendingRegistration.setUserName(request.getUserName());
        pendingRegistration.setDateOfBirth(request.getDateOfBirth());

        pendingRegistration.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        pendingRegistrationService.save(pendingRegistration);

        otpService.sendVerificationOTP(request.getUserEmail());

        log.info("Verification OTP sent to {}", request.getUserEmail());
    }

    @Transactional
    public void verifyRegistration(String userEmail, int otp) {

        boolean verified = otpService.verifyOTP(userEmail, otp);

        if (!verified) {
            pendingRegistrationService.delete(userEmail);
            throw new InvalidOtpException("Invalid OTP.");
        }

        PendingRegistration pending =
                pendingRegistrationService.getByUserEmail(userEmail);

        User user = new User();

        user.setUserName(pending.getUserName());
        user.setPassword(pending.getPassword());

        user.setUserEmail(pending.getUserEmail());
        user.setDateOfBirth(pending.getDateOfBirth());
        userService.saveUser(user);

        pendingRegistrationService.delete(userEmail);

        try {
            sendRegistrationSuccessMail(userEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}", userEmail, e);
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(userEmail);

        String jwt = jwtService.generateToken(userDetails);

        log.info("User {} registered successfully.", userEmail);


    }


    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getUserEmail()
                );

        String jwt = jwtService.generateToken(userDetails);

        log.info("User {} logged in successfully.", request.getUserEmail());

        return jwt;
    }





    private void sendRegistrationSuccessMail(String email) {

        emailService.sendEmail(
                email,
                "Registration Successful",
                """
                Welcome!

                Your account has been created successfully.

                You can now start using Journal App.

                Thank you for registering!
                """
        );
    }
}