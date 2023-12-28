package by.shylau.weatherapp.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16.0"));
//            .withInitScript("db/migration/V1__create_table.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

//    @Test
//    @SneakyThrows
//    void saveLocation() {
//        mockMvc.perform(post("http://localhost:8080/weather/addLocation"))
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    @SneakyThrows
    void showLocation() {
        var result = mockMvc.perform(get("http://localhost:8080/weather/show"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        System.err.println("result");
        assertEquals(result, 1);
    }

//    @Test
//    void saveLocation() {
//        mockMvc.perform(post("http://localhost:8080/weather/addLocation"))
//                .andExpect(status().is2xxSuccessful());
//    }

}