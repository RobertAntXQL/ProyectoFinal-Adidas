package com.adidas.inventario.dto;

import java.math.BigDecimal;
import java.util.List;

import com.adidas.inventario.entity.DetalleVenta;
import com.adidas.inventario.entity.Venta;

public record VentaResponseDTO(
        Venta venta,
        List<DetalleVenta> items,
        BigDecimal total
) {
}
