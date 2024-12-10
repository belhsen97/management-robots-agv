package tn.enova.Configures;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParameterConfig {
    @Value("${myApp.link.Path.robot-image}")
    public String linkRobotPhoto;
}