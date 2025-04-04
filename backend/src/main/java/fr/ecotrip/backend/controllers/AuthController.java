package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.JwtIssuer;
import fr.ecotrip.backend.model.LoginRequest;
import fr.ecotrip.backend.model.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final JwtIssuer jwtIssuer;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String token = jwtIssuer.issue(123456, request.getEmail(), List.of("USER"));

        return LoginResponse
                .builder()
                .accessToken(token)
                .build();
    }

}
