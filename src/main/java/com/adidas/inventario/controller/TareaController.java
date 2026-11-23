package com.adidas.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.TareaEstadoRequestDTO;
import com.adidas.inventario.dto.TareaRequestDTO;
import com.adidas.inventario.entity.Tarea;
import com.adidas.inventario.service.TareaService;
import com.adidas.inventario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tareas")
@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO', 'VENDEDOR')")
public class TareaController {

    private final TareaService tareaService;
    private final UsuarioService usuarioService;

    public TareaController(
            TareaService tareaService,
            UsuarioService usuarioService) {

        this.tareaService = tareaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Tarea> listar(Authentication usuario) {
        return tareaService.listar(
                id(usuario),
                esAdmin(usuario));
    }

    @GetMapping("/{idTarea}")
    public Tarea obtener(
            @PathVariable("idTarea") String idTarea,
            Authentication usuario) {

        return tareaService.obtener(
                idTarea,
                id(usuario),
                esAdmin(usuario));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Tarea crear(
            @Valid @RequestBody TareaRequestDTO datos) {

        return tareaService.crear(datos);
    }

    @PutMapping("/{idTarea}")
    @PreAuthorize("hasRole('ADMIN')")
    public Tarea editar(
            @PathVariable("idTarea") String idTarea,
            @Valid @RequestBody TareaRequestDTO datos) {

        return tareaService.editar(idTarea, datos);
    }

    @PatchMapping("/{idTarea}/estado")
    public Tarea estado(
            @PathVariable("idTarea") String idTarea,
            @Valid @RequestBody TareaEstadoRequestDTO datos,
            Authentication usuario) {

        return tareaService.estado(
                idTarea,
                datos.estado(),
                id(usuario),
                esAdmin(usuario));
    }

    @DeleteMapping("/{idTarea}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable("idTarea") String idTarea) {

        tareaService.eliminar(idTarea);
    }

    private String id(Authentication usuario) {
        return usuarioService.entidad(usuario.getName()).idUsu;
    }

    private boolean esAdmin(Authentication usuario) {
        return usuario.getAuthorities().stream()
                .anyMatch(autoridad ->
                        "ROLE_ADMIN".equals(autoridad.getAuthority()));
    }
}