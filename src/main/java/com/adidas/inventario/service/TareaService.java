package com.adidas.inventario.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.TareaRequestDTO;
import com.adidas.inventario.entity.Tarea;
import com.adidas.inventario.repository.TareaRepository;
import com.adidas.inventario.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class TareaService {

    private final TareaRepository tareas;
    private final UsuarioRepository usuarios;

    public TareaService(
            TareaRepository tareas,
            UsuarioRepository usuarios) {

        this.tareas = tareas;
        this.usuarios = usuarios;
    }

    public List<Tarea> listar(
            String idUsuario,
            boolean admin) {

        return admin
                ? tareas.findAll(
                        Sort.by(
                                Sort.Direction.DESC,
                                "fecha",
                                "idTarea"))
                : tareas.findByIdUsuarioOrderByFechaDesc(idUsuario);
    }

    public Tarea obtener(
            String idTarea,
            String idUsuario,
            boolean admin) {

        Tarea tarea = tareas.findById(idTarea)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarea inexistente"));

        verificarAcceso(tarea, idUsuario, admin);

        return tarea;
    }

    @Transactional
    public Tarea crear(TareaRequestDTO datos) {
        Tarea tarea = new Tarea();

        tarea.idTarea = UUID.randomUUID().toString();
        tarea.fecha = LocalDateTime.now(ZoneId.of("America/Lima"));
        tarea.estado = "Pendiente";

        aplicarDatos(tarea, datos);

        return tareas.saveAndFlush(tarea);
    }

    @Transactional
    public Tarea editar(
            String idTarea,
            TareaRequestDTO datos) {

        Tarea tarea = bloquear(idTarea);

        aplicarDatos(tarea, datos);

        return tareas.saveAndFlush(tarea);
    }

    @Transactional
    public Tarea estado(
            String idTarea,
            String estado,
            String idUsuario,
            boolean admin) {

        Tarea tarea = bloquear(idTarea);

        verificarAcceso(tarea, idUsuario, admin);

        tarea.estado = estado;

        return tareas.saveAndFlush(tarea);
    }

    @Transactional
    public void eliminar(String idTarea) {
        tareas.delete(bloquear(idTarea));
        tareas.flush();
    }

    private Tarea bloquear(String idTarea) {
        return tareas.bloquearPorId(idTarea)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarea inexistente"));
    }

    private void verificarAcceso(
            Tarea tarea,
            String idUsuario,
            boolean admin) {

        if (!admin && !tarea.idUsuario.equalsIgnoreCase(idUsuario)) {
            throw new AccessDeniedException(
                    "Tarea asignada a otro usuario");
        }
    }

    private void aplicarDatos(
            Tarea tarea,
            TareaRequestDTO datos) {

        tarea.idUsuario =
                usuarios.findById(datos.idUsuario().strip())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Usuario asignado inexistente"))
                        .idUsu;

        tarea.titulo = datos.titulo().strip();

        tarea.descripcion =
                datos.descripcion() == null
                || datos.descripcion().isBlank()
                        ? null
                        : datos.descripcion().strip();
    }
}