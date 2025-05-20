package fr.ecotrip.backend.security;


import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPrincipalService implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur avec l'email " + email + " non trouvé");
        }

        return UserPrincipal
                .builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
                .password(user.getPassword())
                .build();
    }
}
