package com.adidas.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "id_cli", nullable = false, length = 36)
    public String idCli;

    @Column(name = "dni", unique = true, length = 20)
    public String dni;

    @Column(name = "nombre", length = 150)
    public String nombre;

    @Column(name = "telefono", length = 20)
    public String telefono;

    @Column(name = "email", length = 100)
    public String email;

    @Column(name = "direccion", length = 200)
    public String direccion;

    @Column(name = "ciudad", length = 100)
    public String ciudad;
}