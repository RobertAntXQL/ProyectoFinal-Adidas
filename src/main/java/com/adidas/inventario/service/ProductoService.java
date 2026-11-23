package com.adidas.inventario.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.ProductoRequestDTO;
import com.adidas.inventario.entity.Categoria;
import com.adidas.inventario.entity.Producto;
import com.adidas.inventario.repository.CategoriaRepository;
import com.adidas.inventario.repository.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public List<Producto> buscarPorCategoria(String idCategoria) {
        return productoRepository.findByIdCategoria(idCategoria);
    }

    public Optional<Producto> obtener(String idProd) {
        return productoRepository.findById(idProd);
    }

    @Transactional
    public Producto crear(ProductoRequestDTO datos) {
        Producto producto = new Producto();

        producto.idProd = UUID.randomUUID().toString();
        producto.stock = 0;
        producto.stockMinimo = 8;

        aplicarDatos(producto, datos);

        return guardar(producto);
    }

    @Transactional
    public Producto actualizar(
            String idProd,
            ProductoRequestDTO datos) {

        Producto producto = bloquear(idProd);

        aplicarDatos(producto, datos);

        return guardar(producto);
    }

    @Transactional
    public void eliminar(String idProd) {
        Producto producto = bloquear(idProd);

        if (producto.stock != 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Primero registra la salida del stock restante");
        }

        try {
            productoRepository.delete(producto);
            productoRepository.flush();

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar un producto con registros asociados",
                    ex);
        }
    }

    private Producto bloquear(String idProd) {
        return productoRepository.bloquearPorId(idProd)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto inexistente"));
    }

    private Producto guardar(Producto producto) {
        try {
            return productoRepository.saveAndFlush(producto);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un producto con ese código",
                    ex);
        }
    }

    private void aplicarDatos(
            Producto producto,
            ProductoRequestDTO datos) {

        Categoria categoria =
                categoriaRepository.bloquearPorId(
                        datos.idCategoria().strip())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Categoría inexistente"));

        producto.idCategoria = categoria.idCategoria;
        producto.codigo = datos.codigo().strip();
        producto.nombre = datos.nombre().strip();

        producto.talla = validarTalla(
                categoria.tipoTalla,
                datos.talla());

        producto.color = limpiar(datos.color());
        producto.precioCompra = datos.precioCompra();
        producto.precioVenta = datos.precioVenta();
        producto.imagenUrl = limpiar(datos.imagenUrl());

        if (datos.stockMinimo() != null) {
            producto.stockMinimo = datos.stockMinimo();
        }
    }

    private String validarTalla(
            String tipoTalla,
            String valor) {

        String talla = limpiar(valor);

        if (talla != null) {
            talla = talla.toUpperCase(Locale.ROOT);
        }

        switch (tipoTalla) {

            case "NUMERICA" -> {
                if (talla == null
                        || !talla.matches(
                                "[0-9]+(?:[.,][0-9]+| [0-9]+/[0-9]+)?")) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Indica una talla numérica válida");
                }
            }

            case "LETRAS" -> {
                if (talla == null
                        || !Set.of(
                                "XS", "S", "M", "L",
                                "XL", "XXL", "XXXL").contains(talla)) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Usa una talla XS, S, M, L, XL, XXL o XXXL");
                }
            }

            case "SIN_TALLA" -> {
                if (talla != null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Esta categoría no utiliza talla");
                }
            }

            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de talla inválido");
        }

        return talla;
    }

    private String limpiar(String valor) {
        return valor == null || valor.isBlank()
                ? null
                : valor.strip();
    }
}