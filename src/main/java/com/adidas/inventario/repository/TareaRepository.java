package com.adidas.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adidas.inventario.entity.Tarea;

import jakarta.persistence.LockModeType;

public interface TareaRepository
        extends JpaRepository<Tarea, String> {

    List<Tarea> findByIdUsuarioOrderByFechaDesc(String idUsuario);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Tarea t where t.idTarea = :idTarea")
    Optional<Tarea> bloquearPorId(
            @Param("idTarea") String idTarea);
}