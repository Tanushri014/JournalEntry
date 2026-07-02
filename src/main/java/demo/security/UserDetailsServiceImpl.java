package demo.service;

import demo.entity.User;
import demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
private  final UserRepository userRepository;

    public UserDetails loadUserByName(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if(user!=null){
           UserDetails userDetails= org.springframework.security.core.userdetails.User.builder().username(user.getUserName()).password(user.getPassword()).roles(user.getRoles().toArray(new String[0])).build();
            return userDetails;
        }
throw  new UsernameNotFoundException("User not found userName"+userName);
    }


}
