package com.adidas.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, String> {
}