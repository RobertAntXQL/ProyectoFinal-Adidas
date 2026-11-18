package com.adidas.inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.adidas.inventario.entity.Categoria;
import com.adidas.inventario.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }
}