package com.adidas.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.DetalleMovimiento;

public interface DetalleMovimientoRepository
        extends JpaRepository<DetalleMovimiento, String> {

    List<DetalleMovimiento> findByIdMovi(String idMovi);
}