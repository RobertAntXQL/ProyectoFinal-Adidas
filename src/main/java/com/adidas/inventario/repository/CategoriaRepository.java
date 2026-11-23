package com.adidas.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adidas.inventario.entity.Categoria;

import jakarta.persistence.LockModeType;

public interface CategoriaRepository
        extends JpaRepository<Categoria, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Categoria c where c.idCategoria = :idCategoria")
    Optional<Categoria> bloquearPorId(
            @Param("idCategoria") String idCategoria);
}