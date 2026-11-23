package com.adidas.inventario.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trabajadores")
public class Trabajador {

    @Id
    @Column(name = "id_trab", nullable = false, length = 36)
    public String idTrab;

    @Column(name = "dni", nullable = false, unique = true, length = 15)
    public String dni;

    @Column(name = "nombre", nullable = false, length = 100)
    public String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    public String apellido;

    @Column(name = "fecha_nacimiento")
    public LocalDate fechaNacimiento;

    @Column(name = "numero_celu", length = 20)
    public String numeroCelu;

    @Column(name = "direccion", length = 200)
    public String direccion;

    @Column(name = "id_rol", nullable = false, length = 36)
    public String idRol;
}