package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TareaRequestDTO(

        @NotBlank
        @Size(max = 120)
        String titulo,

        @Size(max = 400)
        String descripcion,

        @NotBlank
        @Size(max = 36)
        String idUsuario

) {}
