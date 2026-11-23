package com.adidas.inventario.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(name = "id_usu", nullable = false, length = 36)
    public String idUsu;

    @Column(name = "id_trab", nullable = false, unique = true, length = 36)
    public String idTrab;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    public String username;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 100)
    public String password;
}