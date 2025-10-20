package br.com.pauloultra.authserviceapi.security;

import br.com.pauloultra.authserviceapi.security.dtos.UserDetailsDTO;
import br.com.pauloultra.authserviceapi.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.reponses.AuthenticationResponse;
import models.requests.AuthenticateRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JWTAuthenticationImpl {

    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(final AuthenticateRequest request) {
        try {
            log.info("Authenticating user: {}", request.email());

            final var authentication = new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
            );

            final var authResult = authenticationManager.authenticate(authentication);
            final var userDetails = (UserDetailsDTO) authResult.getPrincipal();

            return buildAuthenticationResponse(userDetails);

        } catch (BadCredentialsException ex) {
            log.error("Error on authenticate user: {}", request.email(), ex);
            throw new BadCredentialsException("Email or password invalid");
        } catch (Exception ex) {
            log.error("Unexpected error during authentication for user: {}", request.email(), ex);
            throw new RuntimeException("Authentication failed");
        }
    }

    private AuthenticationResponse buildAuthenticationResponse(final UserDetailsDTO detailsDTO) {
        log.info("Successfully authenticated user: {}", detailsDTO.getUsername());
        final var token = jwtUtils.generateToken(detailsDTO);
        return AuthenticationResponse.builder()
                .type("JWT")
                .token("Bearer " + token)
                .build();
    }
}