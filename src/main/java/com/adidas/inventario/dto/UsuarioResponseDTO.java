package com.adidas.inventario.dto;

public record UsuarioResponseDTO(
        String idUsu,
        String username,
        String idTrab,
        String nombre,
        String apellido,
        String rol,
        String autoridad
) {
}
