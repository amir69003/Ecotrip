package fr.ecotrip.backend.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder

public class UserResponse {
    private String email;
    private String username;
}
