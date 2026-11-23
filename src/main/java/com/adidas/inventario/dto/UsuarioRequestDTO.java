package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank
        @Size(max = 36)
        String idTrab,

        @NotBlank
        @Size(max = 50)
        String username,

        @Size(min = 8, max = 72)
        String password

) {}