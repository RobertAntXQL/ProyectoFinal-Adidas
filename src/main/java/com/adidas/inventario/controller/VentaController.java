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

import com.adidas.inventario.dto.VentaRequestDTO;
import com.adidas.inventario.dto.VentaResponseDTO;
import com.adidas.inventario.service.UsuarioService;
import com.adidas.inventario.service.VentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
public class VentaController {

    private final VentaService ventaService;
    private final UsuarioService usuarioService;

    public VentaController(
            VentaService ventaService,
            UsuarioService usuarioService) {

        this.ventaService = ventaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<VentaResponseDTO> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{idVenta}")
    public VentaResponseDTO obtener(
            @PathVariable("idVenta") String idVenta) {

        return ventaService.obtener(idVenta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponseDTO registrar(
            @Valid @RequestBody VentaRequestDTO datos,
            Authentication authentication) {

        String idUsuario =
                usuarioService.entidad(authentication.getName()).idUsu;

        return ventaService.registrar(datos, idUsuario);
    }
}