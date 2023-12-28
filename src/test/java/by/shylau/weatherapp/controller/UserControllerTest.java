package by.shylau.weatherapp.controller;

import by.shylau.weatherapp.model.User;
import by.shylau.weatherapp.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@WebMvcTest(AuthController.class)
//@TestConfiguration(proxyBeanMethods = false)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

//    @LocalServerPort
//    private Integer port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15.1-alpine"));


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveUser() throws Exception {
        String requestBody = "{\"name\": \"John\"}";
        mockMvc.perform(post("/users").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User("Alice", "Cooper");
        userRepository.save(user);

        String response = mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).contains("Alice");
    }

    @Test
    void testDeleteUserById() throws Exception {
        User user = new User("Bob", "Dilan");
        userRepository.save(user);

        mockMvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().isOk());

        assertThat(userRepository.count()).isZero();
    }
}
