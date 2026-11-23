package com.adidas.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, String> {

    List<DetalleVenta> findByIdVenta(String idVenta);
}