package com.fernandodev.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegistrationRequestDto(
        @NotBlank(message = "O nome não pode estar em branco.")
        String name,
        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "Formato de e-mail inválido.")
        String email,
        @NotBlank(message = "A senha não pode estar em branco.")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
        String password
) {
}
