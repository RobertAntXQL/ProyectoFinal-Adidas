package com.adidas.inventario.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductoRequestDTO(
        @NotBlank @Size(max = 36) String idCategoria,
        @NotBlank @Size(max = 50) String codigo,
        @NotBlank @Size(max = 150) String nombre,
        @Size(max = 20) String talla,
        @Size(max = 50) String color,
        @PositiveOrZero @Digits(integer = 8, fraction = 2) BigDecimal precioCompra,
        @PositiveOrZero @Digits(integer = 8, fraction = 2) BigDecimal precioVenta,
        @PositiveOrZero Integer stockMinimo,
        @Size(max = 500) String imagenUrl
) {
}