package com.adidas.inventario.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MovimientoRequestDTO(
        @NotBlank @Pattern(regexp = "Entrada|Salida") String tipo,
        @NotBlank @Size(max = 36) String idAlma,
        @Size(max = 36) String idProv,
        @NotBlank @Size(max = 200) String motivo,
        @NotEmpty List<@NotNull @Valid ItemMovimientoRequestDTO> items
) {
}