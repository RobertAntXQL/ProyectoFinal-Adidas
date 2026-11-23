package com.adidas.inventario.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.ItemMovimientoRequestDTO;
import com.adidas.inventario.dto.MovimientoRequestDTO;
import com.adidas.inventario.dto.MovimientoResponseDTO;
import com.adidas.inventario.entity.Almacen;
import com.adidas.inventario.entity.DetalleMovimiento;
import com.adidas.inventario.entity.Movimiento;
import com.adidas.inventario.entity.Producto;
import com.adidas.inventario.repository.AlmacenRepository;
import com.adidas.inventario.repository.DetalleMovimientoRepository;
import com.adidas.inventario.repository.MovimientoRepository;
import com.adidas.inventario.repository.ProductoRepository;
import com.adidas.inventario.repository.ProveedorRepository;

@Service
@Transactional(readOnly = true)
public class InventarioService {

    private final ProductoRepository productos;
    private final MovimientoRepository movimientos;
    private final DetalleMovimientoRepository detalles;
    private final AlmacenRepository almacenes;
    private final ProveedorRepository proveedores;

    public InventarioService(
            ProductoRepository productos,
            MovimientoRepository movimientos,
            DetalleMovimientoRepository detalles,
            AlmacenRepository almacenes,
            ProveedorRepository proveedores) {

        this.productos = productos;
        this.movimientos = movimientos;
        this.detalles = detalles;
        this.almacenes = almacenes;
        this.proveedores = proveedores;
    }

    @Transactional
    public MovimientoResponseDTO registrar(
            MovimientoRequestDTO datos, String idUsuario) {

        validarItemsUnicos(
                datos.items(),
                ItemMovimientoRequestDTO::idProd);

        List<String> ids = datos.items().stream()
                .map(ItemMovimientoRequestDTO::idProd)
                .toList();

        Map<String, Producto> bloqueados = bloquearProductos(ids);

        return guardarMovimiento(datos, idUsuario, bloqueados);
    }

    static <T> void validarItemsUnicos(
            List<T> items, Function<T, String> identificador) {

        Set<String> ids = new HashSet<>();

        for (T item : items) {
            String id = identificador.apply(item)
                    .strip()
                    .toLowerCase(Locale.ROOT);

            if (!ids.add(id)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Agrupa las cantidades de un mismo producto en una línea");
            }
        }
    }

    Map<String, Producto> bloquearProductos(List<String> ids) {
        Map<String, Producto> bloqueados = new HashMap<>();
        Set<String> encontrados = new HashSet<>();

        List<String> ordenados = ids.stream()
                .map(String::strip)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();

        for (String id : ordenados) {
            Producto producto = productos.bloquearPorId(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Producto inexistente " + id));

            if (!encontrados.add(producto.idProd)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Producto repetido en la operación");
            }

            bloqueados.put(id, producto);
        }

        return bloqueados;
    }

    MovimientoResponseDTO guardarMovimiento(
            MovimientoRequestDTO datos,
            String idUsuario,
            Map<String, Producto> bloqueados) {

        Almacen almacen = almacenes.findById(datos.idAlma().strip())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Almacén inexistente"));

        String idProv = null;

        if (datos.idProv() != null && !datos.idProv().isBlank()) {
            idProv = proveedores.findById(datos.idProv().strip())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Proveedor inexistente"))
                    .idProv;
        }

        Movimiento movimiento = new Movimiento();
        movimiento.idMovi = UUID.randomUUID().toString();
        movimiento.fecha = LocalDateTime.now(ZoneId.of("America/Lima"));
        movimiento.tipo = datos.tipo();
        movimiento.idUsuario = idUsuario;
        movimiento.idAlma = almacen.idAlma;
        movimiento.idProv = idProv;
        movimiento.motivo = datos.motivo().strip();

        Movimiento guardado = movimientos.saveAndFlush(movimiento);
        List<DetalleMovimiento> items = new ArrayList<>();

        for (ItemMovimientoRequestDTO dato : datos.items()) {
            Producto producto = bloqueados.get(dato.idProd().strip());

            long variacion = switch (datos.tipo()) {
                case "Entrada" -> (long) dato.cantidad();
                case "Salida" -> -(long) dato.cantidad();
                default -> throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Tipo de movimiento inválido");
            };

            long nuevoStock = producto.stock + variacion;

            if (nuevoStock < 0) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuficiente para " + producto.codigo);
            }

            if (nuevoStock > Integer.MAX_VALUE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cantidad fuera del rango para " + producto.codigo);
            }

            producto.stock = (int) nuevoStock;

            DetalleMovimiento detalle = new DetalleMovimiento();
            detalle.idMovDet = UUID.randomUUID().toString();
            detalle.idMovi = guardado.idMovi;
            detalle.idProduc = producto.idProd;
            detalle.cantidad = dato.cantidad();
            detalle.precio = dato.precio();

            items.add(detalle);
        }

        detalles.saveAll(items);

        return new MovimientoResponseDTO(guardado, items);
    }

    public List<MovimientoResponseDTO> listar() {
        return movimientos.findAll(
                Sort.by(Sort.Direction.DESC, "fecha", "idMovi"))
                .stream()
                .map(this::presentar)
                .toList();
    }

    public MovimientoResponseDTO obtener(String idMovi) {
        Movimiento movimiento = movimientos.findById(idMovi)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movimiento inexistente"));

        return presentar(movimiento);
    }

    private MovimientoResponseDTO presentar(Movimiento movimiento) {
        return new MovimientoResponseDTO(
                movimiento,
                detalles.findByIdMovi(movimiento.idMovi));
    }
}