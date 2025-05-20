package fr.ecotrip.backend.service;

import fr.ecotrip.backend.security.UserPrincipal;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.exeption.ForbiddenActionException;
import fr.ecotrip.backend.exeption.InternalServerErrorException;
import fr.ecotrip.backend.exeption.UnauthenticatedUserException;
import fr.ecotrip.backend.exeption.UserNotFoundException;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;




import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void createUser(UserRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new ForbiddenActionException("Un utilisateur avec cet email existe déjà.");
        }

        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new ForbiddenActionException("Un utilisateur avec cet username existe déjà.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encodedPassword) // hash
                .role("USER")
                .build();

        userRepository.save(user);
    }

    public List<UserResponse> findAll() {
        try {
            return userRepository.findAll()
                    .stream()
                    .map(user -> UserResponse.builder()
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new InternalServerErrorException("Erreur lors de la récupération des utilisateurs.");
        }
    }


    public UserResponse findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur avec l'ID " + id + " non trouvé."));
    
        return UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
    


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec l'ID " + id + " non trouvé."));
    }


    public void updateUser(Long id, UserRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }


        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur avec l'ID " + id + " non trouvé."));

        
        if (!principal.getUserId().equals(id)) {
            throw new ForbiddenActionException("Vous ne pouvez modifier que votre propre compte.");
        }

        User existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new ForbiddenActionException("Cet email est déjà utilisé par un autre utilisateur.");
        }

        User existingUserWithUsername = userRepository.findByUsername(request.getUsername());

        if (existingUserWithUsername != null && !existingUserWithUsername.getId().equals(id)) {
            throw new ForbiddenActionException("Ce nom d'utilisateur est déjà utilisé par un autre utilisateur.");
        }
        

        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());


    
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    
        userRepository.save(user);
    }
    
}
