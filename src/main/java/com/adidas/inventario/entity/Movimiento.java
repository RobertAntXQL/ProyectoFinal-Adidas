package com.adidas.inventario.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    @Column(name = "id_movi", nullable = false, length = 36)
    public String idMovi;

    @Column(name = "fecha", nullable = false)
    public LocalDateTime fecha;

    @Column(name = "tipo", nullable = false, length = 10)
    public String tipo;

    @Column(name = "id_usuario", nullable = false, length = 36)
    public String idUsuario;

    @Column(name = "id_alma", nullable = false, length = 36)
    public String idAlma;

    @Column(name = "id_prov", length = 36)
    public String idProv;

    @Column(name = "motivo", nullable = false, length = 200)
    public String motivo;
}