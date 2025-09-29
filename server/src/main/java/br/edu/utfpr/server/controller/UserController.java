package br.edu.utfpr.server.controller;

import br.edu.utfpr.server.model.User;
import br.edu.utfpr.server.security.dto.UserResponseDTO;
import br.edu.utfpr.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(maxAge = 3600)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest()
                        .body("Erro: Email já está em uso!");
            }

            User savedUser = userService.save(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UserResponseDTO(savedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
