package com.adidas.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, String> {
}
