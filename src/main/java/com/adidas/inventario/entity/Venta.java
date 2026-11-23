package com.adidas.inventario.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @Column(name = "id_venta", nullable = false, length = 36)
    public String idVenta;

    @Column(name = "fecha", nullable = false)
    public LocalDateTime fecha;

    @Column(name = "id_cli", length = 36)
    public String idCli;

    @Column(name = "id_usu", nullable = false, length = 36)
    public String idUsu;

    @Column(name = "direccion_entrega", length = 200)
    public String direccionEntrega;

    @Column(name = "distrito_entrega", length = 100)
    public String distritoEntrega;

    @Column(name = "referencia_entrega", length = 200)
    public String referenciaEntrega;

    @Column(name = "forma_pago", length = 50)
    public String formaPago;

    @Column(name = "estado_pedido", nullable = false, length = 50)
    public String estadoPedido;
}