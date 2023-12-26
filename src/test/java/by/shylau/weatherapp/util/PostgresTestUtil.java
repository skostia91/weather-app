package by.shylau.weatherapp.util;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class PostgresTestUtil {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    static {
        postgres.start();
    }

}
