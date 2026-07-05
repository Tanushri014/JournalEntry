//package demo.serviceTests;
//
//import demo.dto.UserResponse;
//import demo.entity.User;
//import demo.exception.UserNotFoundException;
//import demo.repository.UserRepository;
//import demo.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//
//@ExtendWith(MockitoExtension.class)
////tells this class to insert the mock objects
////test does not interact with database directly ...
//public class UserServiceTest {
//
//    //mock repositories
//@Mock
//private UserRepository userRepository;
//
//@Mock private PasswordEncoder passwordEncoder;
//
//
////injectmocks for services
//    @InjectMocks
//    private UserService userService;
//
//
////getallusers() method test
//    @Test
//    void getAllUsers_shouldReturnAllUsers(){
//        //arrange --->dummy data in db
//        User user1=new User();
//        user1.setUserEmail("neha@gmail.com");
//
//        User user2=new User();
//        user2.setUserEmail("neha@gmail.com");
//
//        List<User> users_list=List.of(user1,user2);
//
//
//        when(userRepository.findAll()).thenReturn(users_list);
//
//
//        //act
//        List<User> result=userService.getAllUsers();
//
//        //check
//        assertEquals(users_list,result);
//
//
//        //verify
//        verify(userRepository).findAll();
//
//    }
//
//    @Test
//    void findById_shouldReturnUser_WhenUserExists() {
//
//        User user = new User();
//        user.setId(1L);
//        user.setUserEmail("neha@gmail.com");
//
//        when(userRepository.findById(1L))
//                .thenReturn(Optional.of(user));
//
//        User result = userService.findById(1L);
//
//        assertEquals(user, result);
//
//        verify(userRepository).findById(1L);
//    }
//    @Test
//    void findById_shouldThrowException_WhenUserDoesNotExist() {
//
//        when(userRepository.findById(1L))
//                .thenReturn(Optional.empty());
//
//        assertThrows(
//                UserNotFoundException.class,
//                () -> userService.findById(1L)
//        );
//
//        verify(userRepository).findById(1L);
//    }
//
//
//    @Test
//    void findByUserName_shouldReturnUser() {
//
//        User user = new User();
//        user.setUserEmail("maya");
//
//        when(userRepository.findByUserEmail("maya"))
//                .thenReturn(Optional.of(user));
//
//        User result = userService.findByUserEmail("maya");
//
//        assertEquals(user, result);
//
//        verify(userRepository).findByUserEmail("maya");
//    }
//    @Test
//    void findByUserName_shouldThrowException_WhenUserNotFound() {
//
//        when(userRepository.findByUserEmail("maya"))
//                .thenReturn(Optional.empty());
//
//        assertThrows(
//                UserNotFoundException.class,
//                () -> userService.findByUserEmail("maya")
//        );
//
//        verify(userRepository).findByUserEmail("maya");
//    }
//    @Test
//    void saveUser_shouldSaveUser() {
//
//        User user = new User();
//        user.setUserEmail("maya");
//
//        when(userRepository.save(user))
//                .thenReturn(user);
//
//        User result = userService.saveUser(user);
//
//        assertEquals(user, result);
//
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void updateUser_shouldUpdatePassword(){
//        //arrange
//        User existingUser=new User();
//        existingUser.setUserEmail("maya mehara");
//        existingUser.setPassword("before change password");
//        existingUser.setId(1L);
//
//
//
//        User updatedUser=new User();
//        updatedUser.setUserEmail("maya mehara");
//        updatedUser.setPassword("after change password");
//        updatedUser.setId(1L);
//
//
//        when(userRepository.findByUserEmail("maya mehara")).thenReturn(Optional.of(existingUser));
//when(passwordEncoder.encode("after change password")).thenReturn("encodedPassword");
//        when(userRepository.save(existingUser))
//                .thenReturn(existingUser);
//
//        //act
//        UserResponse result =userService.updateUser("maya mehara",updatedUser);
//
//        //check
//        assertEquals("maya mehara",result.getUserEmail());
//
//
//        //verify
//        verify(userRepository).findByUserEmail("maya mehara");
//        verify(passwordEncoder).encode("after change password");
//        verify(userRepository).save(existingUser);
//
//    }
//@Test
//    void deleteByUser_shouldDeleteByUserName(){
//        //assert
//    User user=new User();
//    user.setUserEmail("ab");
//    user.setPassword("fakePassword");
//    user.setId(1L);
//
//    when(userRepository.findByUserEmail("ab")).thenReturn(Optional.of(user));
//
//    //act
//    userService.deleteUser("ab");
//
//
//    //check
//
//
//    //verify
//    verify(userRepository).delete(user);
//
//}
//
//
//
//
//
//}
///**
// * When the mocked userRepository.findAll() method is called, then return the users list.
// *
// * Since userRepository is a mock, it has no real database behind it. If you don't tell it what to return, it returns a default value (null, 0, false, etc.).
// *
// * So in unit tests, we stub the mock's behavior using when(...).thenReturn(...).
// */
