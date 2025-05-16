package br.com.pauloultra.userserviceapi.controller.impl;

import br.com.pauloultra.userserviceapi.controller.UserController;
import br.com.pauloultra.userserviceapi.service.UserService;
import lombok.RequiredArgsConstructor;
import models.reponses.UserResponse;
import models.requests.CreateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> findById(String id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @Override
    public ResponseEntity<Void> save(final CreateUserRequest createUserRequest) {
        userService.save(createUserRequest);
        return ResponseEntity.status(CREATED.value()).build();
    }
}
