package com.adidas.inventario.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.ProveedorRequestDTO;
import com.adidas.inventario.entity.Proveedor;
import com.adidas.inventario.repository.ProveedorRepository;

@Service
@Transactional(readOnly = true)
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Proveedor obtener(String idProv) {
        return proveedorRepository.findById(idProv)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor inexistente"));
    }

    @Transactional
    public Proveedor crear(ProveedorRequestDTO datos) {
        Proveedor proveedor = new Proveedor();
        proveedor.idProv = UUID.randomUUID().toString();

        aplicarDatos(proveedor, datos);
        return guardar(proveedor);
    }

    @Transactional
    public Proveedor actualizar(String idProv, ProveedorRequestDTO datos) {
        Proveedor proveedor = obtener(idProv);

        aplicarDatos(proveedor, datos);
        return guardar(proveedor);
    }

    @Transactional
    public void eliminar(String idProv) {
        Proveedor proveedor = obtener(idProv);

        try {
            proveedorRepository.delete(proveedor);
            proveedorRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El proveedor tiene pedidos o movimientos asociados",
                    ex);
        }
    }

    private void aplicarDatos(Proveedor proveedor, ProveedorRequestDTO datos) {
        proveedor.rucDni = datos.rucDni().strip();
        proveedor.razonSocial = datos.razonSocial().strip();
        proveedor.contacto = datos.contacto();
        proveedor.telefono = datos.telefono();
        proveedor.email = datos.email();
        proveedor.direccion = datos.direccion();
        proveedor.ciudad = datos.ciudad();
        proveedor.pais = datos.pais();
    }

    private Proveedor guardar(Proveedor proveedor) {
        try {
            return proveedorRepository.saveAndFlush(proveedor);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un proveedor con ese RUC o DNI",
                    ex);
        }
    }
}