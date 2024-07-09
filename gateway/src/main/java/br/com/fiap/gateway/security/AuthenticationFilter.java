package br.com.fiap.gateway.security;

import br.com.fiap.gateway.integration.LoginServiceApi;
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

    private final LoginServiceApi loginServiceApi;

    public AuthenticationFilter(TokenService tokenService, LoginServiceApi loginServiceApi) {
        super(CustomFilterConfig.class);
        this.tokenService = tokenService;
        this.loginServiceApi = loginServiceApi;
    }

    @Override
    public GatewayFilter apply(CustomFilterConfig config) {
        return (exchange, chain) -> {

            var token = this.recoverToken(exchange.getRequest());
            if (token != null) {

                String email = tokenService.validateToken(token);
                if (email == null) {
                    return onError(exchange, "Token Inválido", HttpStatus.UNAUTHORIZED);
                }

                var usuario = loginServiceApi.consultarPorEmail(email);

                if (usuario != null
                    && usuario.roles() != null
                    && usuario.roles().stream().anyMatch(r -> r.equalsIgnoreCase(config.getRole()))) {

                    // Adiciona o Id do usuário para passar na requisição para os microserviços.
                    var request = exchange.getRequest()
                            .mutate()
                            .header("usuarioId", usuario.id().toString())
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());
                } else {
                    return onError(exchange, "Usuário não possui permissão para acessar o endereço solicitado.",
                            HttpStatus.UNAUTHORIZED);
                }

            } else {
                return onError(exchange, "Token Não informado", HttpStatus.BAD_REQUEST);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String errorBody = "{\"error\": \"" + errorMessage + "\"}";
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(errorBody.getBytes())));
    }

    private String recoverToken(ServerHttpRequest request) {
        var authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}