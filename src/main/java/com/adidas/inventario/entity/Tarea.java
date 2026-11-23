package com.adidas.inventario.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @Column(name = "id_tarea", nullable = false, length = 36)
    public String idTarea;

    @Column(name = "titulo", nullable = false, length = 120)
    public String titulo;

    @Column(name = "descripcion", length = 400)
    public String descripcion;

    @Column(name = "estado", nullable = false, length = 20)
    public String estado;

    @Column(name = "fecha", nullable = false)
    public LocalDateTime fecha;

    @Column(name = "id_usuario", nullable = false, length = 36)
    public String idUsuario;
}