package demo.service;

import demo.dto.UserResponse;
import demo.entity.User;
import demo.exception.UserNotFoundException;
import demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get all users.
     * Mainly used by the Admin module.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Find user by id.
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id {}", id);
                    return new UserNotFoundException("User not found");
                });
    }

    /**
     * Find user by username.
     */
    public User findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", userEmail);
                    return new UserNotFoundException("User not found: " + userEmail);
                });
    }

    /**
     * Get logged-in user's profile.
     */
    public UserResponse getUserProfile(String userEmail) {
        User user = findByUserEmail(userEmail);
        return mapToUserResponse(user);
    }

    /**
     * Save user.
     */
    @Transactional
    public User saveUser(User user) {
        log.info("Creating user {}", user.getUserEmail());
        return userRepository.save(user);
    }

    /**
     * Update logged-in user's profile.
     */
    @Transactional
    public UserResponse updateUser(String userEmail, User updatedUser) {

        log.info("Updating user {}", userEmail);

        User existingUser = findByUserEmail(userEmail);

        existingUser.setUserEmail(updatedUser.getUserEmail());

        if (updatedUser.getPassword() != null &&
                !updatedUser.getPassword().isBlank()) {

            existingUser.setPassword(
                    passwordEncoder.encode(updatedUser.getPassword())
            );
        }

        User savedUser = userRepository.save(existingUser);

        return mapToUserResponse(savedUser);
    }

    /**
     * Delete logged-in user.
     */
    @Transactional
    public void deleteUser(String userEmail) {

        User user = findByUserEmail(userEmail);

        log.info("Deleting user {}", user.getUserName());

        userRepository.delete(user);
    }

    public boolean existsByUserEmail(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    /**
     * Convert User entity to UserResponse DTO.
     */
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getDateOfBirth()

        );
    }
}