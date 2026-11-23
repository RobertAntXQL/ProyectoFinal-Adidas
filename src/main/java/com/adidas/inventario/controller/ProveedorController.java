package com.adidas.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.ProveedorRequestDTO;
import com.adidas.inventario.entity.Proveedor;
import com.adidas.inventario.service.ProveedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorService.listar();
    }

    @GetMapping("/{idProv}")
    public Proveedor obtener(@PathVariable("idProv") String idProv) {
        return proveedorService.obtener(idProv);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proveedor crear(@Valid @RequestBody ProveedorRequestDTO datos) {
        return proveedorService.crear(datos);
    }

    @PutMapping("/{idProv}")
    public Proveedor actualizar(
            @PathVariable("idProv") String idProv,
            @Valid @RequestBody ProveedorRequestDTO datos) {

        return proveedorService.actualizar(idProv, datos);
    }

    @DeleteMapping("/{idProv}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("idProv") String idProv) {
        proveedorService.eliminar(idProv);
    }
}