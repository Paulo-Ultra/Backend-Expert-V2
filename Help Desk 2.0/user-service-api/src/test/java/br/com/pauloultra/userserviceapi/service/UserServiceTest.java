package br.com.pauloultra.userserviceapi.service;

import br.com.pauloultra.userserviceapi.entity.User;
import br.com.pauloultra.userserviceapi.mapper.UserMapper;
import br.com.pauloultra.userserviceapi.repository.UserRepository;
import models.exceptions.ResourceNotFoundException;
import models.reponses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    void whenCallFindByIdWithValidIdThenReturnUserResponse() {

        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));

        final var response = userService.findById("1");

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(userRepository, times(1)).findById(anyString());
        verify(mapper, times(1)).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        try {
            userService.findById("1");
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
        }

        verify(userRepository, times(1)).findById(anyString());
        verify(mapper, never()).fromEntity(any(User.class));
    }
}