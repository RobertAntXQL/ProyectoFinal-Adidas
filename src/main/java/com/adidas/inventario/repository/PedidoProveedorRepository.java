package com.adidas.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adidas.inventario.entity.PedidoProveedor;

import jakarta.persistence.LockModeType;

public interface PedidoProveedorRepository
        extends JpaRepository<PedidoProveedor, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PedidoProveedor p where p.idPedido = :idPedido")
    Optional<PedidoProveedor> bloquearPorId(
            @Param("idPedido") String idPedido);
}
