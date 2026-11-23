package com.adidas.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @Column(name = "id_rol", nullable = false, length = 36)
    public String idRol;

    @Column(name = "nombre_rol", nullable = false, unique = true, length = 50)
    public String nombreRol;
}