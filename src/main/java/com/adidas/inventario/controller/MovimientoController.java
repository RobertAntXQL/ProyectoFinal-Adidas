package com.adidas.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.MovimientoRequestDTO;
import com.adidas.inventario.dto.MovimientoResponseDTO;
import com.adidas.inventario.service.InventarioService;
import com.adidas.inventario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimientos")
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
public class MovimientoController {

    private final InventarioService inventarioService;
    private final UsuarioService usuarioService;

    public MovimientoController(
            InventarioService inventarioService,
            UsuarioService usuarioService) {

        this.inventarioService = inventarioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<MovimientoResponseDTO> listar() {
        return inventarioService.listar();
    }

    @GetMapping("/{idMovi}")
    public MovimientoResponseDTO obtener(
            @PathVariable("idMovi") String idMovi) {

        return inventarioService.obtener(idMovi);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponseDTO registrar(
            @Valid @RequestBody MovimientoRequestDTO datos,
            Authentication authentication) {

        String idUsuario =
                usuarioService.entidad(authentication.getName()).idUsu;

        return inventarioService.registrar(datos, idUsuario);
    }
}