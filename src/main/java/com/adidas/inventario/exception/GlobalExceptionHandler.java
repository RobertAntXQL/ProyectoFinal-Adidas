package com.adidas.inventario.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> estado(
            ResponseStatusException ex) {

        String mensaje = ex.getReason() == null
                ? "Solicitud rechazada"
                : ex.getReason();

        return respuesta(ex.getStatusCode(), mensaje);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(
            MethodArgumentNotValidException ex) {

        Map<String, String> campos = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                campos.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", 400,
                        "message", "Revisa los datos enviados",
                        "errors", campos));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(
            HttpMessageNotReadableException ex) {

        return respuesta(
                HttpStatus.BAD_REQUEST,
                "El cuerpo JSON tiene un formato inválido");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> autenticacion(
            AuthenticationException ex) {

        return respuesta(
                HttpStatus.UNAUTHORIZED,
                "Usuario o contraseña incorrectos");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> permisos(
            AccessDeniedException ex) {

        return respuesta(
                HttpStatus.FORBIDDEN,
                "Permiso insuficiente");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridad(
            DataIntegrityViolationException ex) {

        return respuesta(
                HttpStatus.CONFLICT,
                "Los datos están duplicados o tienen registros asociados");
    }

    private ResponseEntity<Map<String, Object>> respuesta(
            HttpStatusCode status,
            String mensaje) {

        return ResponseEntity.status(status).body(
                Map.of(
                        "status", status.value(),
                        "message", mensaje));
    }
}