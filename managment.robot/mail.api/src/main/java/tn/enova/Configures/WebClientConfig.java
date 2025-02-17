package tn.enova.Configures;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
//    @Bean
//    public WebClient.Builder webClientBuilder (){  return WebClient.builder();  }
//    @Bean
//    public WebClient webClient(){  return WebClient.builder().build();  }

    //    @Bean
//    public WebClient webClient() {
//        return  WebClient.builder().baseUrl("http://localhost:8087/management-robot-avg/notfication").build();
//    }
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
}