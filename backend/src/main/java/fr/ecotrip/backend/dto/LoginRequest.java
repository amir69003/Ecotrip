package fr.ecotrip.backend.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
@Getter
@Builder
@AllArgsConstructor

public class LoginRequest {
    private final String email;
    private final String password;
}

