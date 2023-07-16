package ru.gransoft.kafka;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Kuznetsovka 14.07.2023
 */
@SpringBootApplication
@EnableEurekaServer
@OpenAPIDefinition
public class SpringApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationStart.class, args);
    }
}