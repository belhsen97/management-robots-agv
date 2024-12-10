package tn.enova.AppRunners;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Order(value = 1)
@Component
@RequiredArgsConstructor
public class BeanStartup implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
       // String currentDirectory = System.getProperty("user.dir");
       // System.out.println("Current directory: " + currentDirectory);
    }
}