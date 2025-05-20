package fr.ecotrip.backend.dto;

import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
@Builder
public class LoginResponse {

    private final String accessToken;
    private final String username;
    private final String email;
    private final List<String> roles;

}
