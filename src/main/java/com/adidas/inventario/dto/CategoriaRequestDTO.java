package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(

        @NotBlank
        @Size(max = 50)
        String nombre,

        @NotBlank
        @Pattern(regexp = "NUMERICA|LETRAS|SIN_TALLA")
        String tipoTalla

) {}
