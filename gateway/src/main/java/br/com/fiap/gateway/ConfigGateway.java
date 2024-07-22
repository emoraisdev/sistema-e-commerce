package br.com.fiap.gateway;

import br.com.fiap.gateway.routes.MsCarrinhoComprasRoutes;
import br.com.fiap.gateway.routes.MsItemRoutes;
import br.com.fiap.gateway.routes.MsLoginRoutes;
import br.com.fiap.gateway.routes.MsPagamentosRoutes;
import br.com.fiap.gateway.security.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ConfigGateway {

    private final AuthenticationFilter authenticationFilter;

    private final MsLoginRoutes loginRoutes;

    private final MsPagamentosRoutes pagamentosRoutes;

    private final MsItemRoutes itemRoutes;

    private MsCarrinhoComprasRoutes carrinhoComprasRoutes;

    @Bean
    public RouteLocator custom(RouteLocatorBuilder builder){

        var routerBuilder = builder.routes();

        // Cada serviço terá a própria implementação da configuração de rotas.
        loginRoutes.createRoutes(routerBuilder, authenticationFilter);
        pagamentosRoutes.createRoutes(routerBuilder, authenticationFilter);
        itemRoutes.createRoutes(routerBuilder, authenticationFilter);
        carrinhoComprasRoutes.createRoutes(routerBuilder, authenticationFilter);

        return routerBuilder.build();
    }

}
