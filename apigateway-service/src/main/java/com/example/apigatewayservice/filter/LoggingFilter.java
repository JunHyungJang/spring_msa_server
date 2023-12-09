package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

            GatewayFilter filter = new OrderedGatewayFilter((exchange,chain) -> {

            ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
            ServerHttpResponse response = (ServerHttpResponse) exchange.getResponse();

            log.info("Logging filter baseMessage : request id -> {}",config.getBaseMessage());

            if (config.isPostLogger()) {
                log.info("Logging PRE filter Start : request id -> {}",request.getId());

            }
            return chain.filter(exchange).then(Mono.fromRunnable(()-> {
                if (config.postLogger) {
                    log.info("Logging Post filter : response code -> {}", response.getStatusCode());

                }
            }));

            }, Ordered.HIGHEST_PRECEDENCE);

            return filter;

    }
    @Data
    public static class Config {
        private String baseMessage;

        private boolean preLogger;
        private boolean postLogger;
        private String sayhello;
    }
}
