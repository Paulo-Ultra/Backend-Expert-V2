package br.com.pauloultra.userserviceapi.mapper;

import br.com.pauloultra.userserviceapi.entity.User;
import models.reponses.UserResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {

    UserResponse fromEntity(final User entity);
}
