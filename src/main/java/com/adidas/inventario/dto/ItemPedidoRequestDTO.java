package com.adidas.inventario.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ItemPedidoRequestDTO(

        @NotBlank
        @Size(max = 36)
        String idProd,

        @NotNull
        @Positive
        Integer cantidad,

        @NotNull
        @PositiveOrZero
        @Digits(integer = 8, fraction = 2)
        BigDecimal precioCompra

) {}
