package com.adidas.inventario.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas_detalle")
public class DetalleVenta {

    @Id
    @Column(name = "id_detalle", nullable = false, length = 36)
    public String idDetalle;

    @Column(name = "id_venta", nullable = false, length = 36)
    public String idVenta;

    @Column(name = "id_prod", nullable = false, length = 36)
    public String idProd;

    @Column(name = "cantidad", nullable = false)
    public Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    public BigDecimal precioUnitario;
}