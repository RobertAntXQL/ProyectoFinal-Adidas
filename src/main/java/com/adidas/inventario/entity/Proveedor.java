package com.adidas.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @Column(name = "id_prov", nullable = false, length = 36)
    public String idProv;

    @Column(name = "ruc_dni", nullable = false, unique = true, length = 20)
    public String rucDni;

    @Column(name = "razon_social", nullable = false, length = 150)
    public String razonSocial;

    @Column(name = "contacto", length = 100)
    public String contacto;

    @Column(name = "telefono", length = 20)
    public String telefono;

    @Column(name = "email", length = 100)
    public String email;

    @Column(name = "direccion", length = 200)
    public String direccion;

    @Column(name = "ciudad", length = 100)
    public String ciudad;

    @Column(name = "pais", length = 100)
    public String pais;
}