package fr.ecotrip.backend.service;

import fr.ecotrip.backend.dto.UserRequest;
import fr.ecotrip.backend.dto.UserResponse;
import fr.ecotrip.backend.model.User;
import fr.ecotrip.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .build())
                .toList();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
