package com.adidas.inventario.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimientos_detalle")
public class DetalleMovimiento {

    @Id
    @Column(name = "id_mov_det", nullable = false, length = 36)
    public String idMovDet;

    @Column(name = "id_movi", nullable = false, length = 36)
    public String idMovi;

    @Column(name = "id_produc", nullable = false, length = 36)
    public String idProduc;

    @Column(name = "cantidad", nullable = false)
    public Integer cantidad;

    @Column(name = "precio", precision = 10, scale = 2)
    public BigDecimal precio;
}