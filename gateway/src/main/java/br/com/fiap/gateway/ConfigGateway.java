package br.com.fiap.gateway;

import br.com.fiap.gateway.routes.MsLoginRoutes;
import br.com.fiap.gateway.security.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

@Configuration
@AllArgsConstructor
public class ConfigGateway {

    private final AuthenticationFilter authenticationFilter;
    private final MsLoginRoutes loginRoutes;

    @Bean
    public RouteLocator custom(RouteLocatorBuilder builder){

        var routerBuilder = builder.routes();

        // Cada serviço terá a própria implementação da configuração de rotas.
        loginRoutes.createRoutes(routerBuilder, authenticationFilter);

        return routerBuilder.build();
    }

}
