package fr.ecotrip.backend.model;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class LoginResponse {

    private final String accessToken;

}
