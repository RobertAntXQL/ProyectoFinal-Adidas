package com.adidas.inventario.service;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.CambioPasswordRequestDTO;
import com.adidas.inventario.dto.PerfilRequestDTO;
import com.adidas.inventario.dto.PerfilResponseDTO;
import com.adidas.inventario.dto.UsuarioResponseDTO;
import com.adidas.inventario.entity.Trabajador;
import com.adidas.inventario.entity.Usuario;
import com.adidas.inventario.repository.TrabajadorRepository;
import com.adidas.inventario.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class PerfilService {

    private final UsuarioRepository usuarios;
    private final TrabajadorRepository trabajadores;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public PerfilService(
            UsuarioRepository usuarios,
            TrabajadorRepository trabajadores,
            UsuarioService usuarioService,
            PasswordEncoder passwordEncoder) {

        this.usuarios = usuarios;
        this.trabajadores = trabajadores;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    public PerfilResponseDTO obtener(String username) {

        Usuario usuario = usuarioService.entidad(username);

        Trabajador trabajador =
                trabajadores.findById(usuario.idTrab)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Trabajador inexistente"));

        return presentar(usuario, trabajador);
    }

    @Transactional
    public PerfilResponseDTO actualizar(
            String username,
            PerfilRequestDTO datos) {

        Usuario usuario = bloquearUsuario(username);

        Trabajador trabajador =
                trabajadores.bloquearPorId(usuario.idTrab)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Trabajador inexistente"));

        trabajador.nombre = datos.nombre().strip();
        trabajador.apellido = datos.apellido().strip();
        trabajador.fechaNacimiento = datos.fechaNacimiento();
        trabajador.numeroCelu = limpiar(datos.numeroCelu());
        trabajador.direccion = limpiar(datos.direccion());

        trabajadores.saveAndFlush(trabajador);

        return presentar(usuario, trabajador);
    }

    @Transactional
    public void cambiarPassword(
            String username,
            CambioPasswordRequestDTO datos) {

        if (datos.passwordActual()
                .getBytes(StandardCharsets.UTF_8).length > 72
                || datos.passwordNueva()
                .getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cada contraseña debe ocupar como máximo 72 bytes");
        }

        Usuario usuario = bloquearUsuario(username);

        if (!passwordEncoder.matches(
                datos.passwordActual(),
                usuario.password)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña actual es incorrecta");
        }

        usuario.password =
                passwordEncoder.encode(datos.passwordNueva());

        usuarios.saveAndFlush(usuario);
    }

    private Usuario bloquearUsuario(String username) {

        return usuarios.bloquearPorUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario inexistente"));
    }

    private PerfilResponseDTO presentar(
            Usuario usuario,
            Trabajador trabajador) {

        UsuarioResponseDTO resumen =
                usuarioService.presentar(usuario);

        return new PerfilResponseDTO(
                usuario.idUsu,
                usuario.username,
                trabajador.idTrab,
                trabajador.dni,
                trabajador.nombre,
                trabajador.apellido,
                trabajador.fechaNacimiento,
                trabajador.numeroCelu,
                trabajador.direccion,
                resumen.rol(),
                resumen.autoridad());
    }

    private String limpiar(String valor) {

        return valor == null || valor.isBlank()
                ? null
                : valor.strip();
    }
}