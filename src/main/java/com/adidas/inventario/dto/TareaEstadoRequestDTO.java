package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TareaEstadoRequestDTO(

        @NotBlank
        @Pattern(regexp = "Pendiente|En progreso|Completada")
        String estado

) {}