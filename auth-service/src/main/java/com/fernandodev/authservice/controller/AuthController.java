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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationRequestDto newUser) {
        User createdUser = authService.registerUser(newUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
