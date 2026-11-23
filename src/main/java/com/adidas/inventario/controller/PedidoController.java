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

import com.adidas.inventario.dto.PedidoRequestDTO;
import com.adidas.inventario.dto.PedidoResponseDTO;
import com.adidas.inventario.dto.RecepcionRequestDTO;
import com.adidas.inventario.service.PedidoProveedorService;
import com.adidas.inventario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
public class PedidoController {

    private final PedidoProveedorService pedidoService;
    private final UsuarioService usuarioService;

    public PedidoController(
            PedidoProveedorService pedidoService,
            UsuarioService usuarioService) {

        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<PedidoResponseDTO> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/{idPedido}")
    public PedidoResponseDTO obtener(
            @PathVariable("idPedido") String idPedido) {

        return pedidoService.obtener(idPedido);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO crear(
            @Valid @RequestBody PedidoRequestDTO datos,
            Authentication authentication) {

        String idUsuario =
                usuarioService.entidad(authentication.getName()).idUsu;

        return pedidoService.crear(datos, idUsuario);
    }

    @PostMapping("/{idPedido}/recibir")
    public PedidoResponseDTO recibir(
            @PathVariable("idPedido") String idPedido,
            @Valid @RequestBody RecepcionRequestDTO datos,
            Authentication authentication) {

        String idUsuario =
                usuarioService.entidad(authentication.getName()).idUsu;

        return pedidoService.recibir(
                idPedido,
                datos.idAlma(),
                idUsuario);
    }

    @PostMapping("/{idPedido}/cancelar")
    public PedidoResponseDTO cancelar(
            @PathVariable("idPedido") String idPedido) {

        return pedidoService.cancelar(idPedido);
    }
}