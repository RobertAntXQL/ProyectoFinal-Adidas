package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambioPasswordRequestDTO(

        @NotBlank
        @Size(max = 72)
        String passwordActual,

        @NotBlank
        @Size(min = 8, max = 72)
        String passwordNueva

) {}