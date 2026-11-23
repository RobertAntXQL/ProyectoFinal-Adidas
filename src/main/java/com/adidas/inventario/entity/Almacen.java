package com.adidas.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "almacenes")
public class Almacen {

    @Id
    @Column(name = "id_alma", nullable = false, length = 36)
    public String idAlma;

    @Column(name = "letra_zona", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    public String letraZona;

    @Column(name = "descripcion", length = 100)
    public String descripcion;
}