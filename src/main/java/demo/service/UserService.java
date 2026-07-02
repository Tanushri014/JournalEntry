package demo.service;

import demo.entity.User;
import demo.exception.UserNotFoundException;
import demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //if we want to add details about the code or prin ton console then we are going to use this
//Logger logger = LoggerFactory.getLogger(UserRepository.class);
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
    public User findByUserName(String userName) {

        return userRepository.findByUserName(userName).orElseThrow(()->
                {log.warn("User not found :{}",userName);
                    return new UserNotFoundException("user not found"+userName);
                });
    }

    /**
     * Save user.
     */
    @Transactional
    public User saveUser(User user) {
log.info("creating user {}",user.getUserName());
        return userRepository.save(user);
    }

    /**
     * Update logged-in user's profile.
     */
    @Transactional
    public User updateUser(String username,
                           User updatedUser) {
        log.info("updating user {}",username);
        User existingUser = findByUserName(username);

        existingUser.setUserName(updatedUser.getUserName());

        if (updatedUser.getPassword() != null
                && !updatedUser.getPassword().isBlank()) {

            existingUser.setPassword(
                    passwordEncoder.encode(updatedUser.getPassword())
            );
        }

        return userRepository.save(existingUser);
    }

    /**
     * Delete logged-in user.
     */
    @Transactional
    public void deleteUser(String username) {

        User user = findByUserName(username);
        log.info("deleting user {}",user.getUserName());
        userRepository.delete(user);
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }
}
//any method that modifies  the database should be trascational
