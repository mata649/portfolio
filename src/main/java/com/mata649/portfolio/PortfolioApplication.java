package com.mata649.portfolio;

import com.mata649.portfolio.user.dtos.CreateUserRequest;
import com.mata649.portfolio.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PortfolioApplication implements CommandLineRunner {
    @Value("${custom.admin.email}")
    private String adminEmail;

    @Value("${custom.admin.password}")
    private String adminPassword;

    private final UserService userService;

    public PortfolioApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            log.info("Creating the Administrator User {}", adminEmail);
            userService.create(new CreateUserRequest(adminEmail, adminPassword));
            log.info("The administrator user has been created successfully");
        } catch (Exception exc) {
            log.warn("Error creating the user: {}", exc.getMessage());
        }


    }
}
