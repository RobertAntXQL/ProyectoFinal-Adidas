package com.adidas.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, String> {
}
