package br.com.pauloultra.authserviceapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.enums.ProfileEnum;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@AllArgsConstructor
@Getter
@Document
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private Set<ProfileEnum> profiles;
}
