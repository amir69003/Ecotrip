package fr.ecotrip.backend.dto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class LoginRequest {
    private final String email;
    private final String password;

}
