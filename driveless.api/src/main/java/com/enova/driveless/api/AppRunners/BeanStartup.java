package com.enova.driveless.api.AppRunners;



import com.enova.driveless.api.Configures.MQTTClientConfig;
import com.enova.driveless.api.Services.MQTTService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;




@Order(value = 1)//Register beanStartup bean
@Component
@RequiredArgsConstructor
public class BeanStartup implements CommandLineRunner {
    @Override
    public void run(String... args) {}
}