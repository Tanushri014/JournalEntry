//package demo.controllerTests;
//
//
//import demo.config.TestSecurityConfig;
//import demo.controller.UserController;
//import demo.entity.User;
//import demo.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//
//import static org.mockito.Mockito.when;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//
//@WebMvcTest(UserController.class)
//@Import(TestSecurityConfig.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserService userService;
//
//
//    @Test
//    void getProfile_shouldReturnProfile() throws Exception {
//
//        User user = new User();
//        user.setUserName("abc");
//
//        when(userService.findByUserName("abc"))
//                .thenReturn(user);
//
//        mockMvc.perform(
//                        get("/user/profile")
//                                .with(user("abc"))
//                )
//                .andExpect(status().isOk());
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
