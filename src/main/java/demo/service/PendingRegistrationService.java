package demo.service;

import demo.entity.PendingRegistration;
import demo.exception.PendingRegistrationExpiredException;
import demo.exception.PendingRegistrationNotFoundException;
import demo.repository.PendingRegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PendingRegistrationService {

    private static final int EXPIRY_MINUTES = 2;

    private final PendingRegistrationRepository pendingRegistrationRepository;

    public void save(PendingRegistration pendingRegistration) {

        LocalDateTime now = LocalDateTime.now();

        log.info("Saving createdAt: {}", now);
        log.info("Saving expiryTime: {}", now.plusMinutes(EXPIRY_MINUTES));

        pendingRegistration.setCreatedAt(now);
        pendingRegistration.setExpiryTime(now.plusMinutes(EXPIRY_MINUTES));

        pendingRegistrationRepository.save(pendingRegistration);
    }

    public PendingRegistration getByUserEmail(String userEmail) {

        PendingRegistration pendingRegistration = pendingRegistrationRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new PendingRegistrationNotFoundException(
                                "Pending registration not found."
                        ));

        if (pendingRegistration.getExpiryTime().isBefore(LocalDateTime.now())) {

            pendingRegistrationRepository.delete(pendingRegistration);

            throw new PendingRegistrationExpiredException(
                    "Pending registration has expired. Please register again."
            );
        }

        return pendingRegistration;
    }

    @Transactional
    public void delete(String userEmail) {

        pendingRegistrationRepository.findByUserEmail(userEmail)
                .ifPresent(pendingRegistrationRepository::delete);

        log.info("Pending registration deleted for {}", userEmail);
    }
    /**
     * Returns true only if a non-expired pending registration exists.
     */
    public boolean exists(String userEmail) {

        return pendingRegistrationRepository.findByUserEmail(userEmail)
                .map(pending -> {

                    if (pending.getExpiryTime().isBefore(LocalDateTime.now())) {
                        pendingRegistrationRepository.delete(pending);
                        // Force delete to execute immediately
                        pendingRegistrationRepository.flush();
                        return false;
                    }

                    return true;
                })
                .orElse(false);
    }
}