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
public class MsPagamentosRoutes {

    @Value("${api.mspagamentos.server}")
    private String msPagamentos;

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter) {
        routerBuilder.route("pagamentos", r -> configurePagamentoRoute(r, authenticationFilter));
    }

    private Buildable<Route> configurePagamentoRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path("/pagamento/api/pagamento/**")
                .and().method(HttpMethod.POST)
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_USER))))
                .uri(msPagamentos);
    }
}