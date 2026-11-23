package com.adidas.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adidas.inventario.entity.Usuario;

import jakarta.persistence.LockModeType;

public interface UsuarioRepository
        extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);

    boolean existsByIdTrab(String idTrab);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Usuario u where lower(u.username) = lower(:username)")
    Optional<Usuario> bloquearPorUsername(
            @Param("username") String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Usuario u where u.idUsu = :idUsu")
    Optional<Usuario> bloquearPorId(
            @Param("idUsu") String idUsu);
}