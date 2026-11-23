package com.adidas.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, String> {
}