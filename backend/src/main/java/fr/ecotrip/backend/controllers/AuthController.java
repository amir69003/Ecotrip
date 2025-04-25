package fr.ecotrip.backend.controllers;

import fr.ecotrip.backend.Security.JWT.JwtIssuer;
import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.LoginRequest;
import fr.ecotrip.backend.dto.LoginResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtIssuer jwtIssuer;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserRequest user) {
        userService.createUser(user);
        return ResponseEntity.ok(authenticateAndGenerateToken(user.getEmail(), user.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest request) {
        LoginResponse response = authenticateAndGenerateToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
    

    private LoginResponse authenticateAndGenerateToken(String email, String password) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

        return LoginResponse.builder().accessToken(token).build();
    }
}
