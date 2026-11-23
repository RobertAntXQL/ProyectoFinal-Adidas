package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecepcionRequestDTO(

        @NotBlank
        @Size(max = 36)
        String idAlma

) {}