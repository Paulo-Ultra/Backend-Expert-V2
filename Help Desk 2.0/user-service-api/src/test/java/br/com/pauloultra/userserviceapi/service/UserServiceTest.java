package br.com.pauloultra.userserviceapi.service;

import br.com.pauloultra.userserviceapi.entity.User;
import br.com.pauloultra.userserviceapi.mapper.UserMapper;
import br.com.pauloultra.userserviceapi.repository.UserRepository;
import models.exceptions.ResourceNotFoundException;
import models.reponses.UserResponse;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static br.com.pauloultra.userserviceapi.creator.CreatorUtils.generateMock;
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
        when(mapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = userService.findById("1");

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(userRepository).findById(anyString());
        verify(mapper).fromEntity(any(User.class));
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

    @Test
    void whenCallFindAllThenReturnListOfUserResponse() {

        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));

        final var response = userService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(userRepository).findAll();
        verify(mapper, times(2)).fromEntity(any(User.class));
    }

    @Test
    void whenCallSaveThenReturnSucess() {
        final var request = generateMock(CreateUserRequest.class);

        when(mapper.fromRequest(any())).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.save(request);

        verify(mapper).fromRequest(request);
        verify(encoder).encode(request.password());
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByEmail(request.email());
    }

    @Test
    void whenCallSaveWithInvalidEmailThenThrowDataIntegrityViolationException() {
        final var request = generateMock(CreateUserRequest.class);
        final var entity = generateMock(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(entity));

        try {
            userService.save(request);
        } catch (Exception e) {
            assertEquals(DataIntegrityViolationException.class, e.getClass());
            assertEquals("User with email: " + request.email() + " already exists", e.getMessage());
        }

        verify(userRepository).findByEmail(request.email());
        verify(mapper, never()).fromRequest(request);
        verify(encoder, never()).encode(request.password());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithInvalidIdThenThrowResourceNotFoundException() {
        final var request = generateMock(UpdateUserRequest.class);

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        try {
            userService.update("1", request);
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Object not found. Id: " + "1" + ", Type: "
                    + UserResponse.class.getSimpleName(), e.getMessage());
        }

        verify(userRepository).findById(anyString());
        verify(mapper, never()).update(any(), any());
        verify(encoder, never()).encode(request.password());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithInvalidEmailThenThrowDataIntegrityViolationException() {
        final var request = generateMock(UpdateUserRequest.class);

        final var entity = generateMock(User.class);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(entity));

        try {
            userService.update("1", request);
        } catch (Exception e) {
            assertEquals(DataIntegrityViolationException.class, e.getClass());
            assertEquals("User with email: " + request.email() + " already exists", e.getMessage());
        }

        verify(userRepository).findById(anyString());
        verify(userRepository.findByEmail(request.email()));
        verify(mapper, never()).update(any(), any());
        verify(encoder, never()).encode(request.password());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithValidParamsThenGetSuccess() {
        final var id = "1";
        final var request = generateMock(UpdateUserRequest.class);
        final var entity = generateMock(User.class).withId(id);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(entity));
        when(mapper.update(any(), any())).thenReturn(entity);
        when(userRepository.save(any(User.class))).thenReturn(entity);

        userService.update(id, request);

        verify(userRepository).findById(id);
        verify(userRepository).findByEmail(request.email());
        verify(mapper).update(request,entity);
        verify(encoder).encode(request.password());
        verify(userRepository).save(any(User.class));
        verify(mapper).fromEntity(any(User.class));
    }
}