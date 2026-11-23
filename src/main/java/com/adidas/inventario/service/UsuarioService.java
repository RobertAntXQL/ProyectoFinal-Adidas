package com.adidas.inventario.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.UsuarioRequestDTO;
import com.adidas.inventario.dto.UsuarioResponseDTO;
import com.adidas.inventario.entity.Rol;
import com.adidas.inventario.entity.Trabajador;
import com.adidas.inventario.entity.Usuario;
import com.adidas.inventario.repository.RolRepository;
import com.adidas.inventario.repository.TrabajadorRepository;
import com.adidas.inventario.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarios;
    private final TrabajadorRepository trabajadores;
    private final RolRepository roles;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarios,
            TrabajadorRepository trabajadores,
            RolRepository roles,
            PasswordEncoder passwordEncoder) {

        this.usuarios = usuarios;
        this.trabajadores = trabajadores;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario entidad(String username) {
        return usuarios.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario inexistente"));
    }

    public UsuarioResponseDTO perfil(String username) {
        return presentar(entidad(username));
    }

    public List<UsuarioResponseDTO> listar() {
        return usuarios.findAll()
                .stream()
                .map(this::presentar)
                .toList();
    }

    public UsuarioResponseDTO obtener(String idUsu) {
        Usuario usuario = usuarios.findById(idUsu)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario inexistente"));

        return presentar(usuario);
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO datos) {

        validarPassword(datos.password());

        Trabajador trabajador =
                trabajadores.bloquearPorId(datos.idTrab().strip())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Trabajador inexistente"));

        if (usuarios.existsByIdTrab(trabajador.idTrab)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El trabajador ya tiene una cuenta");
        }

        Usuario usuario = new Usuario();

        usuario.idUsu = UUID.randomUUID().toString();
        usuario.idTrab = trabajador.idTrab;
        usuario.username = datos.username().strip();
        usuario.password =
                passwordEncoder.encode(datos.password());

        return presentar(guardar(usuario));
    }

    @Transactional
    public UsuarioResponseDTO actualizar(
            String idUsu,
            UsuarioRequestDTO datos) {

        Usuario usuario = bloquear(idUsu);

        if (!usuario.idTrab.equalsIgnoreCase(
                datos.idTrab().strip())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede cambiar el trabajador asociado a una cuenta");
        }

        usuario.username = datos.username().strip();

        if (datos.password() != null) {
            validarPassword(datos.password());

            usuario.password =
                    passwordEncoder.encode(datos.password());
        }

        return presentar(guardar(usuario));
    }

    @Transactional
    public void eliminar(
            String idUsu,
            String usernameActual) {

        Usuario usuario = bloquear(idUsu);
        Usuario actual = entidad(usernameActual);

        if (usuario.idUsu.equalsIgnoreCase(actual.idUsu)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No puedes eliminar tu propia cuenta");
        }

        try {
            usuarios.delete(usuario);
            usuarios.flush();

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar una cuenta con registros asociados",
                    ex);
        }
    }

    public UsuarioResponseDTO presentar(Usuario usuario) {

        Trabajador trabajador =
                trabajadores.findById(usuario.idTrab)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Trabajador inexistente"));

        Rol rol = roles.findById(trabajador.idRol)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Rol inexistente"));

        String autoridad = switch (rol.idRol) {
            case "R001" -> "ROLE_ADMIN";
            case "R002" -> "ROLE_ALMACENERO";
            case "R003" -> "ROLE_VENDEDOR";
            default -> throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Rol sin permisos configurados");
        };

        return new UsuarioResponseDTO(
                usuario.idUsu,
                usuario.username,
                trabajador.idTrab,
                trabajador.nombre,
                trabajador.apellido,
                rol.nombreRol,
                autoridad);
    }

    private Usuario bloquear(String idUsu) {
        return usuarios.bloquearPorId(idUsu)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario inexistente"));
    }

    private Usuario guardar(Usuario usuario) {
        try {
            return usuarios.saveAndFlush(usuario);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de usuario o el trabajador ya tiene una cuenta asociada",
                    ex);
        }
    }

    private void validarPassword(String password) {

        if (password == null
                || password.isBlank()
                || password.length() < 8
                || password.getBytes(
                        StandardCharsets.UTF_8).length > 72) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica una contraseña de al menos 8 caracteres y hasta 72 bytes");
        }
    }
}