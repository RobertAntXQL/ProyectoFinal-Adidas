package com.adidas.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.ProductoRequestDTO;
import com.adidas.inventario.dto.RecomendacionDTO;
import com.adidas.inventario.entity.Producto;
import com.adidas.inventario.service.ProductoService;
import com.adidas.inventario.service.RecomendacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/buscar-avanzado")
    public List<Producto> buscarPorCategoria(
            @RequestParam("categoria") String idCategoria) {

        return productoService.buscarPorCategoria(idCategoria);
    }

    @GetMapping("/{idProd}")
    public ResponseEntity<Producto> obtener(
            @PathVariable("idProd") String idProd) {

        return ResponseEntity.of(productoService.obtener(idProd));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody ProductoRequestDTO datos) {
        return productoService.crear(datos);
    }

    @PutMapping("/{idProd}")
    public Producto actualizar(
            @PathVariable("idProd") String idProd,
            @Valid @RequestBody ProductoRequestDTO datos) {

        return productoService.actualizar(idProd, datos);
    }

    @Autowired
    private RecomendacionService recomendacionService;

    @GetMapping("/{id}/similares")
    public ResponseEntity<List<RecomendacionDTO.ProductoSimilarDTO>> listarSimilares(
            @PathVariable("id") String idProd) {
        List<RecomendacionDTO.ProductoSimilarDTO> similares = recomendacionService.obtenerSimilares(idProd);
        return ResponseEntity.ok(similares);
    }
}