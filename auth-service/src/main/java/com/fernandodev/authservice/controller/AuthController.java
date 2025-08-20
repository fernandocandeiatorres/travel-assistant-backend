package com.fernandodev.authservice.controller;


import com.fernandodev.authservice.dto.AuthResponseDto;
import com.fernandodev.authservice.dto.UserLoginRequestDto;
import com.fernandodev.authservice.dto.UserRegistrationRequestDto;
import com.fernandodev.authservice.model.User;
import com.fernandodev.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequest) {
        return ResponseEntity.ok(authService.login(userLoginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegistrationRequestDto newUser) {
        AuthResponseDto authResponse = authService.registerUser(newUser);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
