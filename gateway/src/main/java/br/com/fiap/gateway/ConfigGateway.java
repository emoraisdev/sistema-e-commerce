package br.com.fiap.gateway;

import br.com.fiap.gateway.security.AuthenticationFilter;
import br.com.fiap.gateway.security.CustomFilterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigGateway {

    @Value("${api.mslogin.server}")
    private String mslogin;

    private final AuthenticationFilter authenticationFilter;

    public ConfigGateway(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator custom(RouteLocatorBuilder builder){
        return builder.routes()
                .route("login", this::configureLoginRoute)
                .build();

    }

    private Buildable<Route> configureLoginRoute(PredicateSpec r) {
        return r.path("/usuarios/**")
                .filters(f -> f.stripPrefix(1).filter(authenticationFilter.apply(new CustomFilterConfig("ADMIN"))))
                .uri(mslogin);
    }
}
