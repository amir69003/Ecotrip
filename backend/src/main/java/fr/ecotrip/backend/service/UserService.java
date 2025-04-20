package fr.ecotrip.backend.service;

import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.exeption.ForbiddenActionException;
import fr.ecotrip.backend.exeption.InternalServerErrorException;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;




import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void createUser(UserRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

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
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur avec l'ID " + id + " non trouvé."));

    
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());


    
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    
        userRepository.save(user);
    }
    
}
