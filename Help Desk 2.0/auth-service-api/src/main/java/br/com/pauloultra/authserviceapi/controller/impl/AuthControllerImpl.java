package br.com.pauloultra.authserviceapi.controller.impl;

import br.com.pauloultra.authserviceapi.controller.AuthController;
import br.com.pauloultra.authserviceapi.security.JWTAuthenticationImpl;
import br.com.pauloultra.authserviceapi.services.RefreshTokenService;
import br.com.pauloultra.authserviceapi.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import models.requests.RefreshTokenRequest;
import models.responses.AuthenticationResponse;
import models.requests.AuthenticateRequest;
import models.responses.RefreshTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(final AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(
                new JWTAuthenticationImpl(jwtUtils, authenticationConfiguration.getAuthenticationManager())
                        .authenticate(request)
                        .withRefreshToken(refreshTokenService.save(request.email()).getId()));
    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        return ResponseEntity.ok().body(refreshTokenService.refreshToken(request.refreshToken()));
    }
}
