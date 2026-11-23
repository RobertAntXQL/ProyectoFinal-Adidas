package com.adidas.inventario.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.inventario.dto.AuthRequestDTO;
import com.adidas.inventario.dto.AuthResponseDTO;
import com.adidas.inventario.dto.UsuarioResponseDTO;
import com.adidas.inventario.security.JwtUtils;
import com.adidas.inventario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            UsuarioService usuarioService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @Valid @RequestBody AuthRequestDTO datos) {

        if (datos.password()
                .getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new BadCredentialsException(
                    "Credenciales inválidas");
        }

        Authentication authentication =
                authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken
                                .unauthenticated(
                                        datos.username().strip(),
                                        datos.password()));

        UsuarioResponseDTO usuario =
                usuarioService.perfil(
                        authentication.getName());

        return new AuthResponseDTO(
                jwtUtils.generateToken(
                        usuario.username(),
                        usuario.idUsu()),
                "Bearer",
                usuario);
    }

    @GetMapping("/perfil")
    public UsuarioResponseDTO perfil(
            Authentication usuario) {

        return usuarioService.perfil(usuario.getName());
    }
}