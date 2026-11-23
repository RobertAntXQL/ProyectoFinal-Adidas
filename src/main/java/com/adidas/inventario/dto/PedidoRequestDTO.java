package com.adidas.inventario.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoRequestDTO(

        @NotBlank
        @Size(max = 36)
        String idProv,

        @Size(max = 200)
        String observacion,

        @NotEmpty
        List<@NotNull @Valid ItemPedidoRequestDTO> items

) {}