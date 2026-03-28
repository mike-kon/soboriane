package com.mikesoft.soboriane.base;

import com.mikesoft.soboriane.config.ConfigTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@Slf4j
@SpringBootTest
@ContextConfiguration(initializers = {PostgresBaseTest.DbInitializer.class},
  classes = {ConfigTest.class})
@TestPropertySource(properties = {"spring.liquibase.enabled=true"})
@ActiveProfiles("test")
public abstract class PostgresBaseTest {

  private static final String TEST_BASE_NAME = "sobor_test";

  public static PostgreSQLContainer<?> postgresSQLContainer;

  static {
    postgresSQLContainer = new PostgreSQLContainer<>("postgres:latest")
      .withDatabaseName(TEST_BASE_NAME)
      .withEnv("PGTZ", "Europe/Moscow")
      .withInitScript("create_schema.sql");
  }

  static class DbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      postgresSQLContainer.start();
      Map<String, String> env = Map.of(
        "spring.datasource.url", postgresSQLContainer.getJdbcUrl(),
        "spring.datasource.username", postgresSQLContainer.getUsername(),
        "spring.datasource.password", postgresSQLContainer.getPassword()
      );
      log.debug(env.toString());
      TestPropertyValues.of(env).applyTo(applicationContext.getEnvironment());
    }
  }
}
