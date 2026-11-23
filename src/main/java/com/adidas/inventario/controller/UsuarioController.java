package com.adidas.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.UsuarioRequestDTO;
import com.adidas.inventario.dto.UsuarioResponseDTO;
import com.adidas.inventario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{idUsu}")
    public UsuarioResponseDTO obtener(
            @PathVariable("idUsu") String idUsu) {

        return usuarioService.obtener(idUsu);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO crear(
            @Valid @RequestBody UsuarioRequestDTO datos) {

        return usuarioService.crear(datos);
    }

    @PutMapping("/{idUsu}")
    public UsuarioResponseDTO actualizar(
            @PathVariable("idUsu") String idUsu,
            @Valid @RequestBody UsuarioRequestDTO datos) {

        return usuarioService.actualizar(idUsu, datos);
    }

    @DeleteMapping("/{idUsu}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable("idUsu") String idUsu,
            Authentication usuario) {

        usuarioService.eliminar(
                idUsu,
                usuario.getName());
    }
}