package by.shylau.weatherapp.controller;

import org.flywaydb.core.internal.resource.classpath.ClassPathResource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.PostgreSQLContainer;
import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {
//
//    @Bean
//    public PostgreSQLContainer postgreSQLContainer() {
//        PostgreSQLContainer container = new PostgreSQLContainer()
//                .withDatabaseName("test")
//                .withUsername("test")
//                .withPassword("test");
//        container.start();
//        return container;
//    }
//
//    @Bean
//    @Primary
//    public DataSource dataSource(PostgreSQLContainer postgreSQLContainer) {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(postgreSQLContainer.getDriverClassName());
//        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
//        dataSource.setUsername(postgreSQLContainer.getUsername());
//        dataSource.setPassword(postgreSQLContainer.getPassword());
//
//        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
//        databasePopulator.addScript(new ClassPathResource("db/migration/V1__create_table.sql"));
//
//        DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource);
//        initializer.setDatabasePopulator(databasePopulator);
//        initializer.afterPropertiesSet();
//
//        return dataSource;
//    }
}