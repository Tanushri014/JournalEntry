package demo.service;

import demo.entity.OtpVerification;
import demo.repository.OtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPService {

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_VALIDITY_MINUTES = 2;

    private final EmailService emailService;
    private final OtpRepository otpRepository;

    /**
     * Generates a random 4-digit OTP.
     */
    public int generateOTP() {
        return 1000 + random.nextInt(9000);
    }

    /**
     * Generates, stores and sends an OTP to the user's email.
     */
    @Transactional
    public void sendVerificationOTP(String userEmail) {

        int otp = generateOTP();

        // Save OTP before sending email
        saveOtp(userEmail, otp);

        try {

            String message = """
                    Hello,

                    Your verification OTP is: %d

                    This OTP is valid for %d minutes.

                    If you did not request this verification, please ignore this email.

                    Regards,
                    Journal App
                    """.formatted(otp, OTP_VALIDITY_MINUTES);

            emailService.sendEmail(
                    userEmail,
                    "Email Verification",
                    message
            );

            log.info("OTP sent successfully to {}", userEmail);

        } catch (Exception e) {

            // Remove OTP if email sending fails
            otpRepository.findByEmail(userEmail)
                    .ifPresent(otpRepository::delete);

            log.error("Failed to send OTP to {}", userEmail, e);

            throw new RuntimeException("Failed to send verification email.", e);
        }
    }

    /**
     * Stores or updates the OTP for a given email.
     */
    @Transactional
    public void saveOtp(String userEmail, int otp) {

        OtpVerification otpVerification = otpRepository
                .findByEmail(userEmail)
                .orElse(new OtpVerification());

        LocalDateTime now = LocalDateTime.now();

        otpVerification.setEmail(userEmail);
        otpVerification.setOtp(otp);
        otpVerification.setCreatedAt(now);
        otpVerification.setExpiryTime(now.plusMinutes(OTP_VALIDITY_MINUTES));

        otpRepository.save(otpVerification);
    }

    /**
     * Verifies the OTP entered by the user.
     */
    @Transactional
    public boolean verifyOTP(String userEmail, int otpReceived) {

        Optional<OtpVerification> optionalOtp =
                otpRepository.findByEmail(userEmail);

        if (optionalOtp.isEmpty()) {
            log.warn("No OTP found for {}", userEmail);
            return false;
        }

        OtpVerification otpVerification = optionalOtp.get();

        if (otpVerification.getExpiryTime().isBefore(LocalDateTime.now())) {

            otpRepository.delete(otpVerification);

            log.warn("OTP expired for {}", userEmail);

            return false;
        }

        if (otpVerification.getOtp() != otpReceived) {

            log.warn("Invalid OTP received for {}", userEmail);

            return false;
        }

        otpRepository.delete(otpVerification);

        log.info("OTP verified successfully for {}", userEmail);
        //send mail successful

        return true;
    }
}