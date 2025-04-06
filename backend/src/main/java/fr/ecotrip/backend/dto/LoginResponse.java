package fr.ecotrip.backend.dto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class LoginResponse {

    private final String accessToken;

}
