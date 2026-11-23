package com.adidas.inventario.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.ClienteRequestDTO;
import com.adidas.inventario.entity.Cliente;
import com.adidas.inventario.repository.ClienteRepository;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente obtener(String idCli) {
        return clienteRepository.findById(idCli)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente inexistente"));
    }

    @Transactional
    public Cliente crear(ClienteRequestDTO datos) {
        Cliente cliente = new Cliente();

        cliente.idCli = UUID.randomUUID().toString();

        aplicarDatos(cliente, datos);

        return guardar(cliente);
    }

    @Transactional
    public Cliente actualizar(String idCli, ClienteRequestDTO datos) {
        Cliente cliente = obtener(idCli);

        aplicarDatos(cliente, datos);

        return guardar(cliente);
    }

    @Transactional
    public void eliminar(String idCli) {
        Cliente cliente = obtener(idCli);

        try {
            clienteRepository.delete(cliente);
            clienteRepository.flush();

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar un cliente con ventas registradas",
                    ex);
        }
    }

    private Cliente guardar(Cliente cliente) {
        try {
            return clienteRepository.saveAndFlush(cliente);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un cliente con ese DNI",
                    ex);
        }
    }

    private void aplicarDatos(
            Cliente cliente,
            ClienteRequestDTO datos) {

        cliente.dni = limpiar(datos.dni());
        cliente.nombre = datos.nombre().strip();
        cliente.telefono = limpiar(datos.telefono());
        cliente.email = limpiar(datos.email());
        cliente.direccion = limpiar(datos.direccion());
        cliente.ciudad = limpiar(datos.ciudad());
    }

    private String limpiar(String valor) {
        return valor == null || valor.isBlank()
                ? null
                : valor.strip();
    }
}