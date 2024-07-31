package com.hari134.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication(exclude = { ServletWebServerFactoryAutoConfiguration.class })
@EnableAsync
@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class }) // Exclude default error handling
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
