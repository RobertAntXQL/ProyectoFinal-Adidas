package com.adidas.inventario.dto;

import java.time.LocalDate;

public record PerfilResponseDTO(

        String idUsu,
        String username,
        String idTrab,
        String dni,
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        String numeroCelu,
        String direccion,
        String rol,
        String autoridad

) {}