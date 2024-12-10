package tn.enova.Config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder,  FilterAuthentificate filterAuthentificate) {
        return builder.routes()
                .route("mail-service",r -> r.path("/management-robot/mail-service/**")
                        .filters(f -> f.filter(filterAuthentificate.apply( new FilterAuthentificate.Config())))
                        .uri("lb://mail-service"))
                .route("trackbot-service",r -> r.path("/management-robot/trackbot-service/**")
                        .filters(f -> f.filter(filterAuthentificate.apply( new FilterAuthentificate.Config())))
                        .uri("lb://trackbot-service"))
                .route("notification-service", r -> r.path("/management-robot/notification-service/**")
                        .filters(f -> f.filter(filterAuthentificate.apply( new FilterAuthentificate.Config())))
                        .uri("lb://notification-service"))
                .route("user-service", r -> r.path("/management-robot/user-service/**").uri("lb://user-service"))
                .route("driveless-service", r -> r.path("/management-robot/driveless-service/**").uri("lb://driveless-service"))
                .route("discovery-service", r -> r.path("/eureka/web").filters(f -> f.setPath("/")).uri("http://localhost:8761"))
                .route("discovery-service-static", r -> r.path("/eureka/**") .uri("http://localhost:8761"))
                .build();
    }
}


