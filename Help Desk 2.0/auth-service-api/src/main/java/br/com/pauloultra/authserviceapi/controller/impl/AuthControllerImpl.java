package br.com.pauloultra.authserviceapi.controller.impl;

import br.com.pauloultra.authserviceapi.controller.AuthController;
import models.reponses.AuthenticationResponse;
import models.requests.AuthenticateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(AuthenticationResponse.builder()
                    .type("Bearer")
                    .token("token")
                .build());
    }
}
