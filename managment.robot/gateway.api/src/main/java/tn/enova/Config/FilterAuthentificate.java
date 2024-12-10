package tn.enova.Config;

//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

//@Slf4j
@Component
public class FilterAuthentificate extends AbstractGatewayFilterFactory<FilterAuthentificate.Config> {
    @Autowired
    private WebClient.Builder webClient;

    public FilterAuthentificate( ) {
        super(Config.class);
    }
    public static class Config { public Config( ) {} /* Put the configuration properties*/  }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
//            System.out.println("||||||||||||||||  "+ request.getPath() +"   |||||||||||||||||");
//            if (!request.getHeaders().containsKey("Authorization")) {
//                ServerHttpResponse response = exchange.getResponse();
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return response.setComplete();
//            }
//            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);
//            System.out.println(token);
            return webClient.build()
                    .get()
                    .uri("http://user-service/management-robot/user-service"+request.getPath())
//                    .header("Authorization",   token)
                    .headers(httpHeaders -> {
                        if (request.getHeaders().containsKey("Authorization")) {
                            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);
                            httpHeaders.set("Authorization", token);
                        }
                    })
                    .retrieve().bodyToMono(Map.class)
                    .map(reponse_body -> {
//                        log.debug("reponse body : " + String.valueOf(reponse_body));
                        System.out.println(reponse_body.toString());
                        exchange.getRequest()
                                .mutate()
                                .header("auth-user-id",reponse_body.get("username").toString()/* String.valueOf(reponse_body)*/);
                        return exchange;
                    }).flatMap(chain::filter).onErrorResume(error -> {
                        HttpStatus errorCode = null;
                        String errorMsg = "";
                        if (error instanceof WebClientResponseException) {
                            WebClientResponseException webCLientException = (WebClientResponseException) error;
                            errorCode = webCLientException.getStatusCode();
                            errorMsg = webCLientException.getStatusText();
//                            log.error( "error code : " +  errorCode +" - error msg : "+errorMsg);
                        } else {
                            errorCode = HttpStatus.BAD_GATEWAY;
                            errorMsg = HttpStatus.BAD_GATEWAY.getReasonPhrase();
//                            log.error( "error code : " +  errorCode +" - error msg : "+errorMsg);
                        }
                        return  onError( exchange,  errorMsg,  errorCode);
                    });
            //  return chain.filter(exchange);
        };
    }
    private Mono<Void> onError(ServerWebExchange exchange, String errorMsg, HttpStatus errorCode)  {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(errorCode);
        byte[] bytes = errorMsg.getBytes(StandardCharsets.UTF_8);
        response.writeWith(Mono.just(bytes).map(t -> dataBufferFactory.wrap(t)));
        return response.setComplete();
    }

}