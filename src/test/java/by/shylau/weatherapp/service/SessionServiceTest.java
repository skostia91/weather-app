package by.shylau.weatherapp.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@Testcontainers
class SessionServiceTest {
    @Container
    private static final PostgreSQLContainer<?> postgresSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");


    @BeforeAll
    public static void setUp() {
        if (!postgresSQLContainer.isRunning()) {
            postgresSQLContainer.start();
        }
    }

    @Test
    public void testDatabaseConnection() {
        // Получаем параметры подключения к контейнеру Postgres SQL
        String jdbcUrl = postgresSQLContainer.getJdbcUrl();
        String username = postgresSQLContainer.getUsername();
        String password = postgresSQLContainer.getPassword();

        // Подключаемся к базе данных и проводим тесты
        // Например, создаем таблицу, вставляем данные и делаем запросы для проверки результатов

        // Закрываем соединение и освобождаем ресурсы
    }

    @AfterAll
    public static void tearDown() {
        if (postgresSQLContainer.isRunning()) {
            postgresSQLContainer.stop();
        }
    }
}