package com.fernandodev.authservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserLoginRequestDto(
        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "Formato de e-mail inválido.")
        String email,
        @NotBlank(message = "A senha não pode estar em branco.")
        String password
) {
}
