package com.adidas.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.ClienteRequestDTO;
import com.adidas.inventario.entity.Cliente;
import com.adidas.inventario.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{idCli}")
    public Cliente obtener(
            @PathVariable("idCli") String idCli) {

        return clienteService.obtener(idCli);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(
            @Valid @RequestBody ClienteRequestDTO datos) {

        return clienteService.crear(datos);
    }

    @PutMapping("/{idCli}")
    public Cliente actualizar(
            @PathVariable("idCli") String idCli,
            @Valid @RequestBody ClienteRequestDTO datos) {

        return clienteService.actualizar(idCli, datos);
    }

    @DeleteMapping("/{idCli}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable("idCli") String idCli) {

        clienteService.eliminar(idCli);
    }
}