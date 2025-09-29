package br.edu.utfpr.server.service;

import br.edu.utfpr.server.model.Role;
import br.edu.utfpr.server.model.User;
import br.edu.utfpr.server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        
        if (user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.ROLE_USER));
        }
        
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
