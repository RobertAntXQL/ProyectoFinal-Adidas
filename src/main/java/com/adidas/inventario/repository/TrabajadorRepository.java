package com.adidas.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adidas.inventario.entity.Trabajador;

import jakarta.persistence.LockModeType;

public interface TrabajadorRepository
        extends JpaRepository<Trabajador, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Trabajador t where t.idTrab = :idTrab")
    Optional<Trabajador> bloquearPorId(
            @Param("idTrab") String idTrab);
}