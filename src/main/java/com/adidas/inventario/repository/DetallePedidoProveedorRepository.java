package com.adidas.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adidas.inventario.entity.DetallePedidoProveedor;

public interface DetallePedidoProveedorRepository
        extends JpaRepository<DetallePedidoProveedor, String> {

    List<DetallePedidoProveedor> findByIdPedido(String idPedido);
}