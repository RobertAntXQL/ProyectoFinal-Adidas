package com.adidas.inventario.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.CambioPasswordRequestDTO;
import com.adidas.inventario.dto.PerfilRequestDTO;
import com.adidas.inventario.dto.PerfilResponseDTO;
import com.adidas.inventario.service.PerfilService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/perfil")
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO', 'VENDEDOR')")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public PerfilResponseDTO obtener(
            Authentication usuario) {

        return perfilService.obtener(usuario.getName());
    }

    @PutMapping
    public PerfilResponseDTO actualizar(
            @Valid @RequestBody PerfilRequestDTO datos,
            Authentication usuario) {

        return perfilService.actualizar(
                usuario.getName(),
                datos);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarPassword(
            @Valid @RequestBody CambioPasswordRequestDTO datos,
            Authentication usuario) {

        perfilService.cambiarPassword(
                usuario.getName(),
                datos);
    }
}