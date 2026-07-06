package demo.service;

import demo.dto.LoginRequest;
import demo.dto.RegisterRequest;
import demo.entity.PendingRegistration;
import demo.entity.User;
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
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    @Transactional
    public void register(RegisterRequest request) {

        log.info("Java LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("Java ZoneId: {}", java.time.ZoneId.systemDefault());
        log.info("Registering user {}", request.getUserEmail());

        if (userService.existsByUserEmail(request.getUserEmail())) {
            throw new UserAlreadyExistsException(
                    "An account with this email already exists. Please log in or use a different email."
            );
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

        try {

            otpService.sendVerificationOTP(request.getUserEmail());

            log.info("Verification OTP sent to {}", request.getUserEmail());

        } catch (Exception ex) {

            pendingRegistrationService.delete(request.getUserEmail());

            log.error(
                    "Registration rolled back for {} because OTP email could not be sent.",
                    request.getUserEmail(),
                    ex
            );

            throw ex;
        }
    }

    @Transactional
    public void verifyRegistration(String userEmail, int otp) {

        otpService.verifyOTP(userEmail, otp);

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

        log.info("User {} registered successfully.", userEmail);
    }
    @Transactional
    public void resendOtp(String userEmail) {

        PendingRegistration pending =
                pendingRegistrationService.getByUserEmail(userEmail);

        otpService.sendVerificationOTP(
                pending.getUserEmail()
        );

        log.info(
                "Verification OTP resent to {}",
                userEmail
        );

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

        return  jwtService.generateToken(userDetails);
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

    @Transactional
    public void deletePendingRegistration(String userEmail) {

        otpService.deleteOtp(userEmail);

        pendingRegistrationService.delete(userEmail);

        log.info("Pending registration deleted for {}", userEmail);

    }


}