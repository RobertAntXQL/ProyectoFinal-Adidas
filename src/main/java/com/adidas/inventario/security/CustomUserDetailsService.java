package com.adidas.inventario.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.UsuarioResponseDTO;
import com.adidas.inventario.entity.Usuario;
import com.adidas.inventario.service.UsuarioService;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return cargar(username, null);
    }

    public UserDetails loadUserByUsernameAndId(
            String username,
            String idUsu) {

        if (idUsu == null || idUsu.isBlank()) {
            throw new UsernameNotFoundException(
                    "Token sin identificador de cuenta");
        }

        return cargar(username, idUsu);
    }

    private UserDetails cargar(
            String username,
            String idUsu) {

        try {
            Usuario usuario =
                    usuarioService.entidad(username);

            if (idUsu != null
                    && !usuario.idUsu.equalsIgnoreCase(idUsu)) {

                throw new UsernameNotFoundException(
                        "El token pertenece a otra cuenta");
            }

            UsuarioResponseDTO perfil =
                    usuarioService.presentar(usuario);

            return User.withUsername(usuario.username)
                    .password(usuario.password)
                    .authorities(perfil.autoridad())
                    .build();

        } catch (ResponseStatusException ex) {
            throw new UsernameNotFoundException(
                    "La cuenta no está disponible",
                    ex);
        }
    }
}