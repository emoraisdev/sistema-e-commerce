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
public class MsLoginRoutes {

    @Value("${api.mslogin.server}")
    private String mslogin;

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter){

        routerBuilder.route("loginConsultaUsuarios", r -> configureLoginConsultaUsuariosRoute(r, authenticationFilter)).
                route("loginCadastro", this::configureLoginCadastroRoute);
    }

    private Buildable<Route> configureLoginConsultaUsuariosRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path("/usuarios/api/usuarios")
                .and().method(HttpMethod.GET)
                .filters(f -> f.stripPrefix(1).filter(authenticationFilter.apply(new CustomFilterConfig("ADMIN"))))
                .uri(mslogin);
    }

    private Buildable<Route> configureLoginCadastroRoute(PredicateSpec r) {
        return r.path("/usuarios/api/usuarios", "/usuarios/api/usuarios/logar")
                .and().method(HttpMethod.POST)
                .filters(f -> f.stripPrefix(1))
                .uri(mslogin);
    }
}
