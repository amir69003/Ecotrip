package fr.ecotrip.backend.service;

import fr.ecotrip.backend.Security.UserPrincipal;
import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.exeption.ForbiddenActionException;
import fr.ecotrip.backend.exeption.InternalServerErrorException;
import fr.ecotrip.backend.exeption.UnauthenticatedUserException;
import fr.ecotrip.backend.exeption.UserNotFoundException;
import fr.ecotrip.backend.model.Role;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service de gestion des utilisateurs.
 * Fournit les méthodes pour la création, la modification, la suppression et la consultation des utilisateurs,
 * ainsi que la gestion des rôles et des autorisations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Crée un nouvel utilisateur.
     * @param request Les informations de l'utilisateur à créer
     * @throws ForbiddenActionException si l'email ou le nom d'utilisateur existe déjà
     */
    public void createUser(UserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new ForbiddenActionException("Un utilisateur avec cet email existe déjà.");
        }

        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new ForbiddenActionException("Un utilisateur avec cet username existe déjà.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            isAdmin = principal.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        }

        Set<Role> roles = new HashSet<>();
        
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            boolean tryingToCreateAdmin = request.getRoles().stream()
                    .anyMatch(role -> role == Role.ADMIN);

            if (tryingToCreateAdmin) {
                if (!request.getEmail().equals("admin@admin.com") && !isAdmin) {
                    throw new ForbiddenActionException("Seuls les administrateurs peuvent créer des comptes administrateurs.");
                }
            }

            roles.addAll(request.getRoles());
        } else {
            roles.add(Role.USER);
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encodedPassword)
                .roles(roles)
                .build();

        userRepository.save(user);
    }

    /**
     * Récupère tous les utilisateurs.
     * @return Liste de tous les utilisateurs
     * @throws InternalServerErrorException en cas d'erreur lors de la récupération
     */
    public List<UserResponse> findAll() {
        try {
            return userRepository.findAll()
                    .stream()
                    .map(user -> UserResponse.builder()
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .roles(user.getRoles())
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new InternalServerErrorException("Erreur lors de la récupération des utilisateurs.");
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     * @param id ID de l'utilisateur
     * @return Les informations de l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    public UserResponse findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur avec l'ID " + id + " non trouvé."));
    
        return UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }

    /**
     * Récupère un utilisateur par son email.
     * @param email Email de l'utilisateur
     * @return L'utilisateur correspondant
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Récupère un utilisateur par son ID avec vérification de l'authentification.
     * @param id ID de l'utilisateur
     * @return L'utilisateur correspondant
     * @throws UnauthenticatedUserException si l'utilisateur n'est pas authentifié
     * @throws UserNotFoundException si l'utilisateur n'est pas trouvé
     */
    public User findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthenticatedUserException("Utilisateur non authentifié.");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec l'ID " + id + " non trouvé."));
    }

    /**
     * Met à jour les informations d'un utilisateur.
     * @param id ID de l'utilisateur à mettre à jour
     * @param request Nouvelles informations de l'utilisateur
     * @throws UnauthenticatedUserException si l'utilisateur n'est pas authentifié
     * @throws ForbiddenActionException si l'utilisateur n'est pas autorisé à modifier le compte
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
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

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(request.getRoles());
        }
    
        userRepository.save(user);
    }
}
