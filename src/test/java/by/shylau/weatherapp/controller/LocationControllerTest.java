package by.shylau.weatherapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class LocationControllerTest {

    @Autowired
    LocationController locationController;

    @Test
    void find() throws Exception {

        assertThrows(IllegalArgumentException.class, () -> {
        });

    }
}