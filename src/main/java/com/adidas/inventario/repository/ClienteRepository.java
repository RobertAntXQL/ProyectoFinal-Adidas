package com.adidas.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
}