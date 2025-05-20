package fr.ecotrip.backend.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder

public class UserRequest {
    private String username;
    private String email;
    private String password;
}
