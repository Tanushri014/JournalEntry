package demo.repository;

import demo.entity.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingRegistrationRepository
        extends JpaRepository<PendingRegistration, Long> {

    Optional<PendingRegistration> findByUserEmail(String userEmail);

    void deleteByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);
}