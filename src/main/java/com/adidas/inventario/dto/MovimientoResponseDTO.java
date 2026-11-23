package com.adidas.inventario.dto;

import java.util.List;

import com.adidas.inventario.entity.DetalleMovimiento;
import com.adidas.inventario.entity.Movimiento;

public record MovimientoResponseDTO(
        Movimiento movimiento,
        List<DetalleMovimiento> items
) {
}