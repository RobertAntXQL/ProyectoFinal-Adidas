package com.adidas.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.Almacen;

public interface AlmacenRepository extends JpaRepository<Almacen, String> {
}