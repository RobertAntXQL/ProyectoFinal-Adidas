package com.adidas.inventario.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.AlmacenRequestDTO;
import com.adidas.inventario.entity.Almacen;
import com.adidas.inventario.repository.AlmacenRepository;

@Service
@Transactional(readOnly = true)
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    public List<Almacen> listar() {
        return almacenRepository.findAll();
    }

    public Almacen obtener(String idAlma) {
        return almacenRepository.findById(idAlma)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Almacén inexistente"));
    }

    @Transactional
    public Almacen crear(AlmacenRequestDTO datos) {
        Almacen almacen = new Almacen();

        almacen.idAlma = UUID.randomUUID().toString();

        aplicarDatos(almacen, datos);

        return almacenRepository.saveAndFlush(almacen);
    }

    @Transactional
    public Almacen actualizar(
            String idAlma,
            AlmacenRequestDTO datos) {

        Almacen almacen = obtener(idAlma);

        aplicarDatos(almacen, datos);

        return almacenRepository.saveAndFlush(almacen);
    }

    @Transactional
    public void eliminar(String idAlma) {
        Almacen almacen = obtener(idAlma);

        try {
            almacenRepository.delete(almacen);
            almacenRepository.flush();

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar un almacén con movimientos registrados",
                    ex);
        }
    }

    private void aplicarDatos(
            Almacen almacen,
            AlmacenRequestDTO datos) {

        almacen.letraZona =
                datos.letraZona().toUpperCase(Locale.ROOT);

        almacen.descripcion =
                datos.descripcion() == null
                || datos.descripcion().isBlank()
                        ? null
                        : datos.descripcion().strip();
    }
}