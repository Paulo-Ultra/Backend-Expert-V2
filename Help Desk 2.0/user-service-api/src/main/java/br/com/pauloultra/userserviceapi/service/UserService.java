package br.com.pauloultra.userserviceapi.service;

import br.com.pauloultra.userserviceapi.mapper.UserMapper;
import br.com.pauloultra.userserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.reponses.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return userMapper.fromEntity(userRepository.findById(id).orElse(null));
    }
}
