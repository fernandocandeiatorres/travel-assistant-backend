package com.fernandodev.authservice.service;

import com.fernandodev.authservice.dto.AuthResponseDto;
import com.fernandodev.authservice.dto.UserLoginRequestDto;
import com.fernandodev.authservice.dto.UserRegistrationRequestDto;
import com.fernandodev.authservice.model.User;
import com.fernandodev.authservice.repository.UserRepository;
import com.fernandodev.authservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public User registerUser(UserRegistrationRequestDto userDto) {
        var user = User.builder()
                .name(userDto.name())
                .email(userDto.email())
                .password(passwordEncoder.encode(userDto.password()))
                .build();
        return userRepository.save(user);
    }

    public AuthResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.email(),
                        userLoginRequestDto.password()
                )
        );
        var user = userRepository.findByEmail(userLoginRequestDto.email()).orElseThrow();
        var token = jwtTokenProvider.generateToken(user);
        return AuthResponseDto.builder()
                .token(token)
                .build();
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
