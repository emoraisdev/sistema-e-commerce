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

    private static final String API_USUARIOS = "/usuarios/api/usuarios";

    private static final String API_ROLES = "/usuarios/api/roles";

    public void createRoutes(RouteLocatorBuilder.Builder routerBuilder, AuthenticationFilter authenticationFilter) {

        routerBuilder.route("loginConsultaUsuarios", r -> configureLoginConsultaUsuariosRoute(r, authenticationFilter))
                        .route("loginCadastro", this::configureLoginCadastroRoute)
                .route("atribuirRoleUsuario", r -> configureAtribuirRoleRoute(r, authenticationFilter))
                .route("removerRoleUsuario", r -> configureRemoverRoleRoute(r, authenticationFilter))
                .route("atualizarRole", r -> configureAtualizarRoleRoute(r, authenticationFilter))
                .route("atualizarUsuario", r -> configureAtualizarUsuarioRoute(r, authenticationFilter));
    }

    private Buildable<Route> configureLoginConsultaUsuariosRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path(API_USUARIOS,API_ROLES)
                .and().method(HttpMethod.GET)
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_ADMIN))))
                .uri(mslogin);
    }

    private Buildable<Route> configureLoginCadastroRoute(PredicateSpec r) {
        return r.path(API_USUARIOS, "/usuarios/api/usuarios/logar")
                .and().method(HttpMethod.POST)
                .filters(f -> f.stripPrefix(1))
                .uri(mslogin);
    }

    private Buildable<Route> configureAtribuirRoleRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path("/usuarios/api/usuarios/atribuir-role"
                ,API_ROLES)
                .and().method(HttpMethod.POST)
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_ADMIN))))
                .uri(mslogin);
    }

    private Buildable<Route> configureRemoverRoleRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path("/usuarios/api/usuarios/remover-role")
                .and().method(HttpMethod.DELETE)
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_ADMIN))))
                .uri(mslogin);
    }

    private Buildable<Route> configureAtualizarRoleRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path(API_ROLES)
                .and().method(HttpMethod.PUT)
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_ADMIN))))
                .uri(mslogin);
    }

    private Buildable<Route> configureAtualizarUsuarioRoute(PredicateSpec r, AuthenticationFilter authenticationFilter) {
        return r.path(API_USUARIOS)
                .and().method(HttpMethod.PUT)
                .filters(f -> f.stripPrefix(1)
                        .filter(authenticationFilter.apply(new CustomFilterConfig(Roles.ROLE_USER))))
                .uri(mslogin);
    }
}
