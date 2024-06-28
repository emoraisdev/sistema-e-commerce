package br.com.fiap.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<CustomFilterConfig> {

    private final TokenService tokenService;

    public AuthenticationFilter(TokenService tokenService){
        super(CustomFilterConfig.class);
        this.tokenService = tokenService;
    }

    @Override
    public GatewayFilter apply(CustomFilterConfig config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();

            var token = this.recoverToken(request);
            if(token != null){

                if (tokenService.validateToken(token) == null) {
                    return onError(exchange, "Token Inválido", HttpStatus.UNAUTHORIZED);
                }

            } else {
                return onError(exchange, "Token Não informado", HttpStatus.BAD_REQUEST);
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String errorBody = "{\"error\": \"" + errorMessage + "\"}";
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(errorBody.getBytes())));
    }

    private String recoverToken(ServerHttpRequest request){
        var authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}