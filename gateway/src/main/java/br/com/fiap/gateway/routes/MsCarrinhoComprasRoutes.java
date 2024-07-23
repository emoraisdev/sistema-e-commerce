package br.com.fiap.gateway.routes;

import br.com.fiap.gateway.security.AuthenticationFilter;
import br.com.fiap.gateway.security.CustomFilterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class MsCarrinhoComprasRoutes {

    @Value("${api.mscarrinhocompras.server}")
    private String msCarrinho;

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter) {
        routerBuilder.route("carrinhocompras", r -> configureCarrinhoComprasRoute(r, authenticationFilter));
    }

    private Buildable<Route> configureCarrinhoComprasRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path("/carrinho/api/carrinho/**")
                .and().method(HttpMethod.values())
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_USER))))
                .uri(msCarrinho);
    }

}
