package demo.security;

import demo.entity.User;
import demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail)
            throws UsernameNotFoundException {

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + userEmail));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())   // JWT subject = email
                .password(user.getPassword())
                .build();
    }
}