package br.com.pauloultra.authserviceapi.controller.impl;

import br.com.pauloultra.authserviceapi.controller.AuthController;
import br.com.pauloultra.authserviceapi.security.JWTAuthenticationImpl;
import br.com.pauloultra.authserviceapi.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import models.reponses.AuthenticationResponse;
import models.requests.AuthenticateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtils jwtUtils;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(final AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(
                new JWTAuthenticationImpl(jwtUtils, authenticationConfiguration.getAuthenticationManager())
                        .authenticate(request));
    }
}
