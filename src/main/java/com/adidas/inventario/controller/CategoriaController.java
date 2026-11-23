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

import com.adidas.inventario.dto.CategoriaRequestDTO;
import com.adidas.inventario.entity.Categoria;
import com.adidas.inventario.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO', 'VENDEDOR')")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(
            CategoriaService categoriaService) {

        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{idCategoria}")
    public Categoria obtener(
            @PathVariable("idCategoria") String idCategoria) {

        return categoriaService.obtener(idCategoria);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crear(
            @Valid @RequestBody CategoriaRequestDTO datos) {

        return categoriaService.crear(datos);
    }

    @PutMapping("/{idCategoria}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    public Categoria actualizar(
            @PathVariable("idCategoria") String idCategoria,
            @Valid @RequestBody CategoriaRequestDTO datos) {

        return categoriaService.actualizar(
                idCategoria,
                datos);
    }

    @DeleteMapping("/{idCategoria}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable("idCategoria") String idCategoria) {

        categoriaService.eliminar(idCategoria);
    }
}