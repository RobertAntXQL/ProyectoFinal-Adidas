package com.adidas.inventario.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos_proveedor")
public class PedidoProveedor {

    @Id
    @Column(name = "id_pedido", nullable = false, length = 36)
    public String idPedido;

    @Column(name = "fecha", nullable = false)
    public LocalDateTime fecha;

    @Column(name = "id_prov", nullable = false, length = 36)
    public String idProv;

    @Column(name = "id_usu", nullable = false, length = 36)
    public String idUsu;

    @Column(name = "estado", nullable = false, length = 50)
    public String estado;

    @Column(name = "observacion", length = 200)
    public String observacion;
}