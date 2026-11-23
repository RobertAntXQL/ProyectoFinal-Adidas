package com.adidas.inventario.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.CategoriaRequestDTO;
import com.adidas.inventario.entity.Categoria;
import com.adidas.inventario.repository.CategoriaRepository;
import com.adidas.inventario.repository.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository categorias;
    private final ProductoRepository productos;

    public CategoriaService(
            CategoriaRepository categorias,
            ProductoRepository productos) {

        this.categorias = categorias;
        this.productos = productos;
    }

    public List<Categoria> listar() {
        return categorias.findAll(Sort.by("nombre"));
    }

    public Categoria obtener(String idCategoria) {
        return categorias.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría inexistente"));
    }

    @Transactional
    public Categoria crear(CategoriaRequestDTO datos) {
        Categoria categoria = new Categoria();

        categoria.idCategoria = UUID.randomUUID().toString();

        aplicarDatos(categoria, datos);

        return guardar(categoria);
    }

    @Transactional
    public Categoria actualizar(
            String idCategoria,
            CategoriaRequestDTO datos) {

        Categoria categoria = bloquear(idCategoria);

        aplicarDatos(categoria, datos);

        return guardar(categoria);
    }

    @Transactional
    public void eliminar(String idCategoria) {
        Categoria categoria = bloquear(idCategoria);

        if (!productos.findByIdCategoria(
                categoria.idCategoria).isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar una categoría con productos registrados");
        }

        try {
            categorias.delete(categoria);
            categorias.flush();

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La categoría tiene registros asociados",
                    ex);
        }
    }

    private Categoria bloquear(String idCategoria) {
        return categorias.bloquearPorId(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría inexistente"));
    }

    private Categoria guardar(Categoria categoria) {
        try {
            return categorias.saveAndFlush(categoria);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una categoría con ese nombre",
                    ex);
        }
    }

    private void aplicarDatos(
            Categoria categoria,
            CategoriaRequestDTO datos) {

        if (!Set.of(
                "NUMERICA",
                "LETRAS",
                "SIN_TALLA").contains(datos.tipoTalla())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de talla inválido");
        }

        if (categoria.tipoTalla != null
                && !categoria.tipoTalla.equals(datos.tipoTalla())
                && !productos.findByIdCategoria(
                        categoria.idCategoria).isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No puedes cambiar el tipo de talla de una categoría con productos");
        }

        categoria.nombre = datos.nombre().strip();
        categoria.tipoTalla = datos.tipoTalla();
    }
}