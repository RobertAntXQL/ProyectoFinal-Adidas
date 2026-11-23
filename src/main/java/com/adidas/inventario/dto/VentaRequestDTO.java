package com.adidas.inventario.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VentaRequestDTO(
        @Size(max = 36) String idCli,
        @NotBlank @Size(max = 36) String idAlma,
        @Size(max = 200) String direccionEntrega,
        @Size(max = 100) String distritoEntrega,
        @Size(max = 200) String referenciaEntrega,
        @NotBlank @Size(max = 50) String formaPago,
        @NotEmpty List<@NotNull @Valid ItemVentaRequestDTO> items
) {
}