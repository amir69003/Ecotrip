package fr.ecotrip.backend.controller;

import fr.ecotrip.backend.dto.LoginRequest;
import fr.ecotrip.backend.dto.LoginResponse;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.security.JWT.JwtIssuer;
import fr.ecotrip.backend.security.UserPrincipal;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtIssuer jwtIssuer;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request) {
        return getAuthResponseEntity(request.getEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody @Validated UserRequest user) {
        userService.createUser(user);
        return getAuthResponseEntity(user.getEmail(), user.getPassword());
    }

    private ResponseEntity<LoginResponse> getAuthResponseEntity(String email, String password) {
        LoginResponse response = authenticateAndGenerateToken(email, password);
        ResponseCookie cookie = ResponseCookie.from("access_token", response.getAccessToken()).httpOnly(false).secure(true).path("/").maxAge(60 * 60 * 24) // 1 day
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    private LoginResponse authenticateAndGenerateToken(String email, String password) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(auth);
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        String token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

        return LoginResponse.builder().accessToken(token).username(principal.getUsername()).email(principal.getEmail()).roles(roles).build();
    }
}
