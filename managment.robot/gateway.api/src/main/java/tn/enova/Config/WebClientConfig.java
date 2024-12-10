package tn.enova.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    //resolve  by annotation   for load balancing using this web client builder it will automatically
    // create the client side load balancer and we will use this client-side load balancing to call
    // the inventory service so in this way even though our order service finds multiple instances of
    // the inventory service like this ex it wont be confused and it will just try to call this inventory
    // service one after another right
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder (){  return WebClient.builder();  }

    // used without client eureka
   //Failed to resolve 'inventory-service' after 6 queries because we have multiple version of the inventory service
   @Bean
   public WebClient webClient(){  return WebClient.builder().build();  }
}
