package com.adidas.inventario.dto;

import java.math.BigDecimal;
import java.util.List;

import com.adidas.inventario.entity.DetallePedidoProveedor;
import com.adidas.inventario.entity.PedidoProveedor;

public record PedidoResponseDTO(

        PedidoProveedor pedido,

        List<DetallePedidoProveedor> items,

        BigDecimal total

) {}