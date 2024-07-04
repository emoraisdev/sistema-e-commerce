package br.com.fiap.gateway.integration;

import br.com.fiap.gateway.exception.BusinessException;
import br.com.fiap.gateway.integration.dto.UsuarioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoginServiceApi {

    @Value("${api.mslogin.server}")
    private String urlMsLogin;

    private static final String API_USUARIOS_BY_EMAIL = "/api/usuarios/by-email/";

    RestTemplate restTemplate;

    public LoginServiceApi(){
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

                var statusCode = response.getStatusCode();
                if (statusCode == HttpStatus.BAD_REQUEST) {
                    // Tratamento personalizado para código 400
                    throw new BusinessException("Bad Request: " +
                            StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
                } else {
                    // Tratamento padrão para outros códigos de erro
                    super.handleError(response);
                }
            }
        });
    }

    public UsuarioDTO consultarPorEmail(String email) {


        var retorno = restTemplate.getForObject(urlMsLogin +
                        API_USUARIOS_BY_EMAIL + email,
                String.class);

        try {
            return new ObjectMapper().readValue(retorno, UsuarioDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
