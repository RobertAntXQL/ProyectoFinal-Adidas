package com.adidas.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemVentaRequestDTO(
        @NotBlank @Size(max = 36) String idProd,
        @NotNull @Positive Integer cantidad
) {
}
