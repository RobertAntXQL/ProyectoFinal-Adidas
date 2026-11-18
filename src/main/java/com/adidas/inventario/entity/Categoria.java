package com.adidas.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @Column(name = "id_categoria", nullable = false, length = 36)
    public String idCategoria;

    @Column(name = "nombre", nullable = false, length = 50)
    public String nombre;

    @Column(name = "tipo_talla", nullable = false, length = 20)
    public String tipoTalla;
}