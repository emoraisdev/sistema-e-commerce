package br.com.fiap.gateway.routes;

import br.com.fiap.gateway.security.AuthenticationFilter;
import br.com.fiap.gateway.security.CustomFilterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;

@Component
public class MsItemRoutes {

    @Value("${api.msitens.server}")
    private String msItens;

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter) {
        routerBuilder.route("itens", r -> configureItemRoute(r, authenticationFilter));
    }

    private Buildable<Route> configureItemRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path("/api/itens/**")
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_USER))))
                .uri(msItens);
    }
}
