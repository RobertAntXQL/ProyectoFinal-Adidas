package com.adidas.inventario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProveedorRequestDTO(
        @NotBlank @Size(max = 20) String rucDni,
        @NotBlank @Size(max = 150) String razonSocial,
        @Size(max = 100) String contacto,
        @Size(max = 20) String telefono,
        @Email @Size(max = 100) String email,
        @Size(max = 200) String direccion,
        @Size(max = 100) String ciudad,
        @Size(max = 100) String pais
) {
}