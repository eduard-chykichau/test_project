package com.example.task.controllers;

import com.example.task.dto.EmailDTO;
import com.example.task.dto.UserDTO;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    /**
     * postgres:15-alpine
     * PostgreSQL version 15 using the lightweight Alpine Linux as the base image
     */
    @ClassRule
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("postgres")
            .withPassword("changeme")
            .withInitScript("scripts/init.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @Test
    public void shouldSaveUser() throws Exception {
        EmailDTO emailDTO = EmailDTO.builder()
                .email("qwerty@gmail.com")
                .build();
        List<EmailDTO> emails = new ArrayList<>();
        emails.add(emailDTO);
        UserDTO userDTO = UserDTO.builder()
                .firstName("first")
                .lastName("last")
                .emails(emails)
                .build();
        HttpEntity<UserDTO> request = new HttpEntity<>(userDTO);

        ResponseEntity<String> result = this.restTemplate.postForEntity("http://localhost:" + port + "/api/users", request, String.class);
        assertThat(result.getStatusCode().value()).isEqualTo(201);

        String location = result.getHeaders().get("Location").get(0);
        String id = location.substring(location.lastIndexOf('/'));

        UserDTO  user = this.restTemplate.getForObject(location, UserDTO.class);
        assertThat(user.getFirstName()).isEqualTo("first");
        assertThat(user.getLastName()).isEqualTo("last");
    }
}