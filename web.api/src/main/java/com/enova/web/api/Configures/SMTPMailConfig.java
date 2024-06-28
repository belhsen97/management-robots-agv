package com.enova.web.api.Configures;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;


@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SMTPMailConfig {
    @Value("${mail.smtp.host}")
    String host; // SMTP server hostname
    @Value("${mail.smtp.auth}")
    boolean enableAuth;
    @Value("${mail.smtp.enableStarttls}")
    boolean enableStarttls;
    @Value("${mail.smtp.username}")
    public String username;
    @Value("${mail.smtp.password}")
    String password;//change accordingly

    @Bean
    public Session getSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", enableAuth);
        properties.put("mail.smtp.starttls.enable", enableStarttls);
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,
                        password);
            }
        });
    }
}
