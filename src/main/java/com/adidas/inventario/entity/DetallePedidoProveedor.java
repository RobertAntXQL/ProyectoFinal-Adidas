package com.adidas.inventario.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos_proveedor_detalle")
public class DetallePedidoProveedor {

    @Id
    @Column(name = "id_ped_det", nullable = false, length = 36)
    public String idPedDet;

    @Column(name = "id_pedido", nullable = false, length = 36)
    public String idPedido;

    @Column(name = "id_prod", nullable = false, length = 36)
    public String idProd;

    @Column(name = "cantidad", nullable = false)
    public Integer cantidad;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    public BigDecimal precioCompra;
}
