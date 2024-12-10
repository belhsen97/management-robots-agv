package tn.enova.Configures;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ParameterConfig {
    @Value("${my-app.file.default-robot-photo}")
    public String defaultRobotPhoto;
}