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
import com.adidas.inventario.dto.ItemPedidoRequestDTO;
import com.adidas.inventario.dto.MovimientoRequestDTO;
import com.adidas.inventario.dto.PedidoRequestDTO;
import com.adidas.inventario.dto.PedidoResponseDTO;
import com.adidas.inventario.entity.DetallePedidoProveedor;
import com.adidas.inventario.entity.PedidoProveedor;
import com.adidas.inventario.entity.Producto;
import com.adidas.inventario.entity.Proveedor;
import com.adidas.inventario.repository.DetallePedidoProveedorRepository;
import com.adidas.inventario.repository.PedidoProveedorRepository;
import com.adidas.inventario.repository.ProveedorRepository;

@Service
@Transactional(readOnly = true)
public class PedidoProveedorService {

    private final PedidoProveedorRepository pedidos;
    private final DetallePedidoProveedorRepository detalles;
    private final ProveedorRepository proveedores;
    private final InventarioService inventario;

    public PedidoProveedorService(
            PedidoProveedorRepository pedidos,
            DetallePedidoProveedorRepository detalles,
            ProveedorRepository proveedores,
            InventarioService inventario) {

        this.pedidos = pedidos;
        this.detalles = detalles;
        this.proveedores = proveedores;
        this.inventario = inventario;
    }

    @Transactional
    public PedidoResponseDTO crear(
            PedidoRequestDTO datos,
            String idUsuario) {

        Proveedor proveedor = proveedores.findById(datos.idProv().strip())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Proveedor inexistente"));

        InventarioService.validarItemsUnicos(
                datos.items(),
                ItemPedidoRequestDTO::idProd);

        List<String> ids = datos.items().stream()
                .map(ItemPedidoRequestDTO::idProd)
                .toList();

        Map<String, Producto> productos =
                inventario.bloquearProductos(ids);

        PedidoProveedor pedido = new PedidoProveedor();

        pedido.idPedido = UUID.randomUUID().toString();
        pedido.fecha = LocalDateTime.now(ZoneId.of("America/Lima"));
        pedido.idProv = proveedor.idProv;
        pedido.idUsu = idUsuario;
        pedido.estado = "Pendiente";
        pedido.observacion = datos.observacion();

        PedidoProveedor guardado = pedidos.saveAndFlush(pedido);

        List<DetallePedidoProveedor> items = new ArrayList<>();

        for (ItemPedidoRequestDTO dato : datos.items()) {

            Producto producto = productos.get(dato.idProd().strip());

            DetallePedidoProveedor detalle =
                    new DetallePedidoProveedor();

            detalle.idPedDet = UUID.randomUUID().toString();
            detalle.idPedido = guardado.idPedido;
            detalle.idProd = producto.idProd;
            detalle.cantidad = dato.cantidad();
            detalle.precioCompra = dato.precioCompra();

            items.add(detalle);
        }

        detalles.saveAll(items);

        return presentar(guardado, items);
    }

    @Transactional
    public PedidoResponseDTO recibir(
            String idPedido,
            String idAlma,
            String idUsuario) {

        PedidoProveedor pedido = pendiente(idPedido);

        List<DetallePedidoProveedor> items =
                detalles.findByIdPedido(pedido.idPedido);

        if (items.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El pedido no contiene productos");
        }

        List<ItemMovimientoRequestDTO> entrada = items.stream()
                .map(item -> new ItemMovimientoRequestDTO(
                        item.idProd,
                        item.cantidad,
                        item.precioCompra))
                .toList();

        inventario.registrar(
                new MovimientoRequestDTO(
                        "Entrada",
                        idAlma,
                        pedido.idProv,
                        "Recepción del pedido " + pedido.idPedido,
                        entrada),
                idUsuario);

        pedido.estado = "Recibido";

        pedidos.saveAndFlush(pedido);

        return presentar(pedido, items);
    }

    @Transactional
    public PedidoResponseDTO cancelar(String idPedido) {

        PedidoProveedor pedido = pendiente(idPedido);

        pedido.estado = "Cancelado";

        pedidos.saveAndFlush(pedido);

        return presentar(
                pedido,
                detalles.findByIdPedido(pedido.idPedido));
    }

    public List<PedidoResponseDTO> listar() {

        return pedidos.findAll(
                Sort.by(Sort.Direction.DESC, "fecha", "idPedido"))
                .stream()
                .map(pedido -> presentar(
                        pedido,
                        detalles.findByIdPedido(pedido.idPedido)))
                .toList();
    }

    public PedidoResponseDTO obtener(String idPedido) {

        PedidoProveedor pedido = pedidos.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido inexistente"));

        return presentar(
                pedido,
                detalles.findByIdPedido(pedido.idPedido));
    }

    private PedidoProveedor pendiente(String idPedido) {

        PedidoProveedor pedido = pedidos.bloquearPorId(idPedido)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido inexistente"));

        if (!"Pendiente".equals(pedido.estado)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El pedido ya fue recibido o cancelado");
        }

        return pedido;
    }

    private PedidoResponseDTO presentar(
            PedidoProveedor pedido,
            List<DetallePedidoProveedor> items) {

        BigDecimal total = items.stream()
                .map(item -> (item.precioCompra == null
                        ? BigDecimal.ZERO
                        : item.precioCompra)
                        .multiply(BigDecimal.valueOf(item.cantidad)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PedidoResponseDTO(pedido, items, total);
    }
}
