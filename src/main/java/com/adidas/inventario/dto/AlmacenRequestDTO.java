package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AlmacenRequestDTO(

        @NotBlank
        @Pattern(regexp = "[A-Za-z]")
        String letraZona,

        @Size(max = 100)
        String descripcion

) {}
