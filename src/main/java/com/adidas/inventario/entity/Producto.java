package com.adidas.inventario.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(name = "id_prod", nullable = false, length = 36)
    public String idProd;

    @Column(name = "id_categoria", nullable = false, length = 36)
    public String idCategoria;

    @Column(name = "codigo", nullable = false, length = 50)
    public String codigo;

    @Column(name = "nombre", nullable = false, length = 150)
    public String nombre;

    @Column(name = "talla", length = 20)
    public String talla;

    @Column(name = "color", length = 50)
    public String color;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    public BigDecimal precioCompra;

    @Column(name = "precio_venta", precision = 10, scale = 2)
    public BigDecimal precioVenta;

    @Column(name = "stock", nullable = false)
    public Integer stock;

    @Column(name = "stock_minimo", nullable = false)
    public Integer stockMinimo;

    @Column(name = "imagen_url", length = 500)
    public String imagenUrl;
}