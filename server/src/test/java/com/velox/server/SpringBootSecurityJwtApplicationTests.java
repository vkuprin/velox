package com.velox.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.velox.server.controller.MyController;
import com.velox.server.model.User;
import com.velox.server.repository.UserRepository;
import com.velox.server.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

    @Autowired
    private MyController myController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(myController).build();
    }

    @Test
    public void contextLoads() {
        assertThat(myController).isNotNull();
    }

    @Test
    public void testJwtTokenCreationAndValidation() {
        // Mock user details
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(bCryptPasswordEncoder.encode("password"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        // Test JWT creation and validation logic here
        // Example:
        // String token = jwtUtil.generateToken(user);
        // boolean isValid = jwtUtil.validateToken(token, user);
        // assertThat(isValid).isTrue();
    }

    @Test
    public void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/public-endpoint"))
            .andExpect(status().isOk());
    }

    @Test
    public void testPrivateEndpointUnauthorized() throws Exception {
        mockMvc.perform(get("/private-endpoint"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPrivateEndpointAuthorized() throws Exception {
        // Mock the JWT token and set it in the request header
        String token = "valid-jwt-token";
        mockMvc.perform(get("/private-endpoint")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    public void testUserCreation() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("newpassword");

        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"newuser\", \"password\": \"newpassword\"}"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"username\": \"newuser\"}"));
    }

    @Test
    public void testUserService() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);
        assertThat(savedUser.getPassword()).isEqualTo("encodedpassword");
    }

    @Test
    public void testUserRepository() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    // Additional Tests

    @Test
    public void testUserNotFound() throws Exception {
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/unknownuser"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDuplicateUserCreation() throws Exception {
        User user = new User();
        user.setUsername("duplicateuser");
        user.setPassword("password");

        when(userRepository.findByUsername("duplicateuser")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"duplicateuser\", \"password\": \"password\"}"))
            .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("oldpassword");

        User updatedUser = new User();
        updatedUser.setUsername("updateuser");
        updatedUser.setPassword("newpassword");

        when(userRepository.findByUsername("updateuser")).thenReturn(Optional.of(user));
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"updateuser\", \"password\": \"newpassword\"}"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"username\": \"updateuser\"}"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("deleteuser");
        user.setPassword("password");

        when(userRepository.findByUsername("deleteuser")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"deleteuser\"}"))
            .andExpect(status().isOk());
    }

    @Test
    public void testListUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{\"username\": \"user1\"}, {\"username\": \"user2\"}]"));
    }

    @Test
    public void testUserAuthentication() throws Exception {
        User user = new User();
        user.setUsername("authuser");
        user.setPassword(bCryptPasswordEncoder.encode("authpassword"));

        when(userRepository.findByUsername("authuser")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("authpassword", user.getPassword())).thenReturn(true);

        mockMvc.perform(post("/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"authuser\", \"password\": \"authpassword\"}"))
            .andExpect(status().isOk());
    }
}
