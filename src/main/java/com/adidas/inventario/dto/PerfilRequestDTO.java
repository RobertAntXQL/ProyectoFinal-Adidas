package com.adidas.inventario.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record PerfilRequestDTO(

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Size(max = 100)
        String apellido,

        @Past
        LocalDate fechaNacimiento,

        @Size(max = 20)
        String numeroCelu,

        @Size(max = 200)
        String direccion

) {}
