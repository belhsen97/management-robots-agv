package com.enova.driveless.api.AppRunners;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Order(value = 1)
@Component
@RequiredArgsConstructor
public class BeanStartup implements CommandLineRunner {
    @Override
    public void run(String... args) {
    }
}