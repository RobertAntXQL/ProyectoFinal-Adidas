package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDTO(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(max = 72) String password
) {
}