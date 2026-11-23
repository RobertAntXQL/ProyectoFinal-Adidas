package com.adidas.inventario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.adidas.inventario.dto.ItemMovimientoRequestDTO;
import com.adidas.inventario.dto.ItemVentaRequestDTO;
import com.adidas.inventario.dto.MovimientoRequestDTO;
import com.adidas.inventario.dto.VentaRequestDTO;
import com.adidas.inventario.dto.VentaResponseDTO;
import com.adidas.inventario.entity.DetalleVenta;
import com.adidas.inventario.entity.Producto;
import com.adidas.inventario.entity.Venta;
import com.adidas.inventario.repository.ClienteRepository;
import com.adidas.inventario.repository.DetalleVentaRepository;
import com.adidas.inventario.repository.VentaRepository;

@Service
@Transactional(readOnly = true)
public class VentaService {

    private final VentaRepository ventas;
    private final DetalleVentaRepository detalles;
    private final ClienteRepository clientes;
    private final InventarioService inventario;

    public VentaService(
            VentaRepository ventas,
            DetalleVentaRepository detalles,
            ClienteRepository clientes,
            InventarioService inventario) {

        this.ventas = ventas;
        this.detalles = detalles;
        this.clientes = clientes;
        this.inventario = inventario;
    }

    @Transactional
    public VentaResponseDTO registrar(
            VentaRequestDTO datos,
            String idUsuario) {

        String idCli = null;

        if (datos.idCli() != null && !datos.idCli().isBlank()) {
            idCli = clientes.findById(datos.idCli().strip())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Cliente inexistente"))
                    .idCli;
        }

        InventarioService.validarItemsUnicos(
                datos.items(),
                ItemVentaRequestDTO::idProd);

        List<String> ids = datos.items().stream()
                .map(ItemVentaRequestDTO::idProd)
                .toList();

        Map<String, Producto> bloqueados =
                inventario.bloquearProductos(ids);

        Venta venta = new Venta();
        venta.idVenta = UUID.randomUUID().toString();
        venta.fecha = LocalDateTime.now(ZoneId.of("America/Lima"));
        venta.idCli = idCli;
        venta.idUsu = idUsuario;
        venta.direccionEntrega = datos.direccionEntrega();
        venta.distritoEntrega = datos.distritoEntrega();
        venta.referenciaEntrega = datos.referenciaEntrega();
        venta.formaPago = datos.formaPago().strip();
        venta.estadoPedido = "Confirmado";

        List<DetalleVenta> items = new ArrayList<>();
        List<ItemMovimientoRequestDTO> salida = new ArrayList<>();

        for (ItemVentaRequestDTO dato : datos.items()) {
            Producto producto = bloqueados.get(dato.idProd().strip());

            if (producto.precioVenta == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Falta el precio de venta para " + producto.codigo);
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.idDetalle = UUID.randomUUID().toString();
            detalle.idVenta = venta.idVenta;
            detalle.idProd = producto.idProd;
            detalle.cantidad = dato.cantidad();
            detalle.precioUnitario = producto.precioVenta;

            items.add(detalle);

            salida.add(new ItemMovimientoRequestDTO(
                    dato.idProd().strip(),
                    dato.cantidad(),
                    producto.precioVenta));
        }

        MovimientoRequestDTO movimiento = new MovimientoRequestDTO(
                "Salida",
                datos.idAlma(),
                null,
                "Venta " + venta.idVenta,
                salida);

        inventario.guardarMovimiento(
                movimiento,
                idUsuario,
                bloqueados);

        Venta guardada = ventas.saveAndFlush(venta);
        detalles.saveAll(items);

        return presentar(guardada, items);
    }

    public List<VentaResponseDTO> listar() {
        return ventas.findAll(
                Sort.by(Sort.Direction.DESC, "fecha", "idVenta"))
                .stream()
                .map(venta -> presentar(
                        venta,
                        detalles.findByIdVenta(venta.idVenta)))
                .toList();
    }

    public VentaResponseDTO obtener(String idVenta) {
        Venta venta = ventas.findById(idVenta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Venta inexistente"));

        return presentar(
                venta,
                detalles.findByIdVenta(venta.idVenta));
    }

    private VentaResponseDTO presentar(
            Venta venta,
            List<DetalleVenta> items) {

        BigDecimal total = items.stream()
                .map(detalle -> detalle.precioUnitario.multiply(
                        BigDecimal.valueOf(detalle.cantidad)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponseDTO(venta, items, total);
    }
}
