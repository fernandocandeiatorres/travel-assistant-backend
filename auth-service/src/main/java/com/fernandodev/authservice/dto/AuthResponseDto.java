package com.fernandodev.authservice.dto;

import lombok.Builder;

@Builder
public record AuthResponseDto(
        String token
) {
}
