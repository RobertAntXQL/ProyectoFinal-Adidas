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

import com.adidas.inventario.dto.AlmacenRequestDTO;
import com.adidas.inventario.entity.Almacen;
import com.adidas.inventario.service.AlmacenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/almacenes")
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO', 'VENDEDOR')")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping
    public List<Almacen> listar() {
        return almacenService.listar();
    }

    @GetMapping("/{idAlma}")
    public Almacen obtener(
            @PathVariable("idAlma") String idAlma) {

        return almacenService.obtener(idAlma);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @ResponseStatus(HttpStatus.CREATED)
    public Almacen crear(
            @Valid @RequestBody AlmacenRequestDTO datos) {

        return almacenService.crear(datos);
    }

    @PutMapping("/{idAlma}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    public Almacen actualizar(
            @PathVariable("idAlma") String idAlma,
            @Valid @RequestBody AlmacenRequestDTO datos) {

        return almacenService.actualizar(idAlma, datos);
    }

    @DeleteMapping("/{idAlma}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable("idAlma") String idAlma) {

        almacenService.eliminar(idAlma);
    }
}