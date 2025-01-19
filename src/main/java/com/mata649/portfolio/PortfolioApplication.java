package com.mata649.portfolio;

import com.mata649.portfolio.user.dtos.CreateUserRequest;
import com.mata649.portfolio.user.service.UserService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
@Theme("custom")
public class PortfolioApplication implements CommandLineRunner, AppShellConfigurator {

    private final Environment environment;

    private final UserService userService;

    public PortfolioApplication(UserService userService, Environment environment) {
        this.environment = environment;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            String adminEmail = environment.getProperty("custom.admin.email");
            String adminPassword = environment.getProperty("custom.admin.password");
            if (adminEmail==null || adminPassword == null){
                throw new RuntimeException(String.format("The admin email or password hasn't been set {email=%s password=%s}",adminEmail, adminPassword));
            }
            log.info("Creating the Administrator User {}", adminEmail);
            userService.create(new CreateUserRequest(adminEmail, adminPassword));
            log.info("The administrator user has been created successfully");
        } catch (Exception exc) {
            log.warn("Error creating the user: {}", exc.getMessage());
        }


    }
}
