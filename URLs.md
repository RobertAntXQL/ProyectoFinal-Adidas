# Guía de URLs y Endpoints del Proyecto - Adidas Inventario

Este documento recopila la totalidad de las URLs y endpoints de la aplicación **Adidas Inventario**, incluyendo el backend principal en **Spring Boot** (puerto `8080`) y el microservicio de Machine Learning en **Python / FastAPI** (puerto `8000`).

---

## 📌 Configuración General y Servidores

| Servicio | Base URL | Tecnología | Autenticación |
|---|---|---|---|
| **Backend Principal** | `http://localhost:8080` | Spring Boot (Java 17) | Bearer Token (JWT en header `Authorization: Bearer <token>`) |
| **Microservicio Recomendador** | `http://localhost:8000` | FastAPI / Python (Uvicorn) | Libre / Interno |

---

## 📑 Tabla Resumen de Endpoints

| Método | URL / Endpoint | Módulo | Roles Permitidos | Código y Resultado |
|---|---|---|---|---|
| `POST` | `/api/auth/login` | Autenticación | Público | `200 OK` - Token JWT y datos del usuario |
| `GET` | `/api/auth/perfil` | Autenticación | Autenticado | `200 OK` - Perfil del usuario autenticado |
| `GET` | `/api/perfil` | Perfil | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Datos personales y laborales del trabajador |
| `PUT` | `/api/perfil` | Perfil | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Perfil actualizado |
| `PATCH` | `/api/perfil/password` | Perfil | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `204 No Content` - Contraseña cambiada |
| `GET` | `/api/usuarios` | Usuarios | `ADMIN` | `200 OK` - Lista de todos los usuarios del sistema |
| `GET` | `/api/usuarios/{idUsu}` | Usuarios | `ADMIN` | `200 OK` - Datos de un usuario por su ID |
| `POST` | `/api/usuarios` | Usuarios | `ADMIN` | `201 Created` - Usuario creado |
| `PUT` | `/api/usuarios/{idUsu}` | Usuarios | `ADMIN` | `200 OK` - Usuario actualizado |
| `DELETE` | `/api/usuarios/{idUsu}` | Usuarios | `ADMIN` | `204 No Content` - Usuario dado de baja |
| `GET` | `/api/clientes` | Clientes | `ADMIN`, `VENDEDOR` | `200 OK` - Lista de clientes |
| `GET` | `/api/clientes/{idCli}` | Clientes | `ADMIN`, `VENDEDOR` | `200 OK` - Detalle de un cliente |
| `POST` | `/api/clientes` | Clientes | `ADMIN`, `VENDEDOR` | `201 Created` - Cliente registrado |
| `PUT` | `/api/clientes/{idCli}` | Clientes | `ADMIN`, `VENDEDOR` | `200 OK` - Cliente actualizado |
| `DELETE` | `/api/clientes/{idCli}` | Clientes | `ADMIN`, `VENDEDOR` | `204 No Content` - Cliente eliminado |
| `GET` | `/api/proveedores` | Proveedores | `ADMIN`, `ALMACENERO` | `200 OK` - Lista de proveedores |
| `GET` | `/api/proveedores/{idProv}` | Proveedores | `ADMIN`, `ALMACENERO` | `200 OK` - Detalle de un proveedor |
| `POST` | `/api/proveedores` | Proveedores | `ADMIN`, `ALMACENERO` | `201 Created` - Proveedor registrado |
| `PUT` | `/api/proveedores/{idProv}` | Proveedores | `ADMIN`, `ALMACENERO` | `200 OK` - Proveedor actualizado |
| `DELETE` | `/api/proveedores/{idProv}` | Proveedores | `ADMIN`, `ALMACENERO` | `204 No Content` - Proveedor eliminado |
| `GET` | `/api/categorias` | Categorías | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Lista de categorías de productos |
| `GET` | `/api/categorias/{idCategoria}` | Categorías | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Detalle de una categoría |
| `POST` | `/api/categorias` | Categorías | `ADMIN`, `ALMACENERO` | `201 Created` - Categoría creada |
| `PUT` | `/api/categorias/{idCategoria}` | Categorías | `ADMIN`, `ALMACENERO` | `200 OK` - Categoría actualizada |
| `DELETE` | `/api/categorias/{idCategoria}` | Categorías | `ADMIN`, `ALMACENERO` | `204 No Content` - Categoría eliminada |
| `GET` | `/api/almacenes` | Almacenes | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Lista de almacenes físicos |
| `GET` | `/api/almacenes/{idAlma}` | Almacenes | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Detalle de un almacén |
| `POST` | `/api/almacenes` | Almacenes | `ADMIN`, `ALMACENERO` | `201 Created` - Almacén creado |
| `PUT` | `/api/almacenes/{idAlma}` | Almacenes | `ADMIN`, `ALMACENERO` | `200 OK` - Almacén actualizado |
| `DELETE` | `/api/almacenes/{idAlma}` | Almacenes | `ADMIN`, `ALMACENERO` | `204 No Content` - Almacén eliminado |
| `GET` | `/api/productos` | Productos | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Catálogo completo de productos |
| `GET` | `/api/productos/buscar-avanzado` | Productos | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Productos filtrados por categoría |
| `GET` | `/api/productos/{idProd}` | Productos | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Detalle completo de un producto |
| `POST` | `/api/productos` | Productos | `ADMIN`, `ALMACENERO` | `201 Created` - Producto registrado |
| `PUT` | `/api/productos/{idProd}` | Productos | `ADMIN`, `ALMACENERO` | `200 OK` - Producto actualizado |
| `GET` | `/api/productos/{id}/similares` | Productos (ML) | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Lista de productos similares sugeridos por IA |
| `GET` | `/api/movimientos` | Movimientos | `ADMIN`, `ALMACENERO` | `200 OK` - Historial de movimientos de stock |
| `GET` | `/api/movimientos/{idMovi}` | Movimientos | `ADMIN`, `ALMACENERO` | `200 OK` - Detalle del movimiento con sus ítems |
| `POST` | `/api/movimientos` | Movimientos | `ADMIN`, `ALMACENERO` | `201 Created` - Movimiento registrado y stock actualizado |
| `GET` | `/api/pedidos` | Pedidos | `ADMIN`, `ALMACENERO` | `200 OK` - Lista de órdenes de compra a proveedores |
| `GET` | `/api/pedidos/{idPedido}` | Pedidos | `ADMIN`, `ALMACENERO` | `200 OK` - Detalle de la orden de compra con ítems |
| `POST` | `/api/pedidos` | Pedidos | `ADMIN`, `ALMACENERO` | `201 Created` - Pedido generado en estado PENDIENTE |
| `POST` | `/api/pedidos/{idPedido}/recibir` | Pedidos | `ADMIN`, `ALMACENERO` | `200 OK` - Pedido RECIBIDO e ingreso automático al almacén |
| `POST` | `/api/pedidos/{idPedido}/cancelar` | Pedidos | `ADMIN`, `ALMACENERO` | `200 OK` - Pedido CANCELADO |
| `GET` | `/api/ventas` | Ventas | `ADMIN`, `VENDEDOR` | `200 OK` - Listado histórico de ventas realizadas |
| `GET` | `/api/ventas/{idVenta}` | Ventas | `ADMIN`, `VENDEDOR` | `200 OK` - Detalle de la venta con comprobante e ítems |
| `POST` | `/api/ventas` | Ventas | `ADMIN`, `VENDEDOR` | `201 Created` - Venta procesada y stock descontado |
| `GET` | `/api/tareas` | Tareas | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Lista de tareas (todas para ADMIN, propias para otros) |
| `GET` | `/api/tareas/{idTarea}` | Tareas | `ADMIN`, `ALMACENERO`, `VENDEDOR` | `200 OK` - Detalle de una tarea |
| `POST` | `/api/tareas` | Tareas | `ADMIN` | `201 Created` - Tarea creada y asignada a un trabajador |
| `PUT` | `/api/tareas/{idTarea}` | Tareas | `ADMIN` | `200 OK` - Tarea editada |
| `PATCH` | `/api/tareas/{idTarea}/estado` | Tareas | `ADMIN` o Asignado | `200 OK` - Estado de tarea modificado |
| `DELETE` | `/api/tareas/{idTarea}` | Tareas | `ADMIN` | `204 No Content` - Tarea eliminada |
| `GET` | `/swagger-ui.html` | Documentación | Público | `200 OK` - Interfaz Swagger UI de Springdoc |
| `GET` | `/v3/api-docs` | Documentación | Público | `200 OK` - Especificación OpenAPI v3 en JSON |
| `GET` | `http://localhost:8000/recomendar/{id_prod}` | Microservicio ML | Público / Interno | `200 OK` - Cálculo de productos similares con TF-IDF |
| `GET` | `http://localhost:8000/docs` | Microservicio ML | Público | `200 OK` - Swagger UI del servicio Python |

---

## 📖 Detalle de Endpoints por Módulo

### 1. Autenticación (`/api/auth`)
Controlador encargado de la emisión y verificación de credenciales de usuario.

* **`POST /api/auth/login`**
  * **Permiso:** Público.
  * **Cuerpo recibido (JSON):** `{"username": "...", "password": "..."}`
  * **Resultado:** `200 OK` con un objeto `AuthResponseDTO` que contiene el token JWT (`token`), tipo de token (`"Bearer"`) y los datos básicos del usuario autenticado (`idUsu`, `username`, `idTrab`, `nombreCompleto`, `rol`).

* **`GET /api/auth/perfil`**
  * **Permiso:** Requiere token JWT.
  * **Resultado:** `200 OK` con el objeto `UsuarioResponseDTO` del usuario en sesión.

---

### 2. Mi Perfil (`/api/perfil`)
Permite a cualquier trabajador autenticado consultar y gestionar su propia información.

* **`GET /api/perfil`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Resultado:** `200 OK` con un objeto `PerfilResponseDTO` con los datos del trabajador (código de trabajador, nombre, apellidos, DNI, teléfono, email, nombre de usuario y rol).

* **`PUT /api/perfil`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Cuerpo recibido (JSON):** `{"nombres": "...", "apellidos": "...", "telefono": "...", "email": "..."}`
  * **Resultado:** `200 OK` con el `PerfilResponseDTO` actualizado.

* **`PATCH /api/perfil/password`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Cuerpo recibido (JSON):** `{"passwordActual": "...", "passwordNueva": "..."}`
  * **Resultado:** `204 No Content` indicando que la contraseña fue validada y actualizada exitosamente.

---

### 3. Gestión de Usuarios (`/api/usuarios`)
Módulo administrativo para el control de cuentas de acceso al sistema.

* **`GET /api/usuarios`**
  * **Permiso:** Solo `ADMIN`.
  * **Resultado:** `200 OK` con una lista JSON (`List<UsuarioResponseDTO>`) con todos los usuarios registrados.

* **`GET /api/usuarios/{idUsu}`**
  * **Permiso:** Solo `ADMIN`.
  * **Parámetro:** `{idUsu}` - Identificador del usuario (ej: `U001`).
  * **Resultado:** `200 OK` con el objeto `UsuarioResponseDTO` correspondiente.

* **`POST /api/usuarios`**
  * **Permiso:** Solo `ADMIN`.
  * **Cuerpo recibido (JSON):** `{"idTrab": "...", "username": "...", "password": "...", "activo": true}`
  * **Resultado:** `201 Created` con el `UsuarioResponseDTO` del usuario creado.

* **`PUT /api/usuarios/{idUsu}`**
  * **Permiso:** Solo `ADMIN`.
  * **Parámetro:** `{idUsu}` - ID del usuario a modificar.
  * **Cuerpo recibido (JSON):** `{"username": "...", "password": "...", "activo": true}`
  * **Resultado:** `200 OK` con el `UsuarioResponseDTO` modificado.

* **`DELETE /api/usuarios/{idUsu}`**
  * **Permiso:** Solo `ADMIN`.
  * **Parámetro:** `{idUsu}` - ID del usuario.
  * **Resultado:** `204 No Content` confirmando la eliminación/baja del usuario (incluye validación que impide auto-eliminación).

---

### 4. Clientes (`/api/clientes`)
Gestión del directorio de clientes de la tienda y su información de contacto.

* **`GET /api/clientes`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Resultado:** `200 OK` con un array JSON de entidades `Cliente`.

* **`GET /api/clientes/{idCli}`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Parámetro:** `{idCli}` - ID del cliente (ej: `CLI001`).
  * **Resultado:** `200 OK` con los datos del `Cliente` solicitado.

* **`POST /api/clientes`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Cuerpo recibido (JSON):** `{"tipoDoc": "DNI", "nroDoc": "...", "nombres": "...", "apellidos": "...", "telefono": "...", "email": "...", "direccion": "..."}`
  * **Resultado:** `201 Created` con la entidad `Cliente` recién creada.

* **`PUT /api/clientes/{idCli}`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Parámetro:** `{idCli}` - ID del cliente.
  * **Cuerpo recibido (JSON):** `ClienteRequestDTO` con los campos actualizados.
  * **Resultado:** `200 OK` con la entidad `Cliente` actualizada.

* **`DELETE /api/clientes/{idCli}`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Parámetro:** `{idCli}` - ID del cliente.
  * **Resultado:** `204 No Content` tras eliminar el registro.

---

### 5. Proveedores (`/api/proveedores`)
Control de empresas proveedoras y fabricantes de productos.

* **`GET /api/proveedores`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Resultado:** `200 OK` con una lista JSON de entidades `Proveedor`.

* **`GET /api/proveedores/{idProv}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idProv}` - ID del proveedor (ej: `PRV001`).
  * **Resultado:** `200 OK` con la información detallada del `Proveedor`.

* **`POST /api/proveedores`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Cuerpo recibido (JSON):** `{"ruc": "...", "razonSocial": "...", "contacto": "...", "telefono": "...", "email": "...", "direccion": "..."}`
  * **Resultado:** `201 Created` con la entidad `Proveedor` creada.

* **`PUT /api/proveedores/{idProv}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idProv}` - ID del proveedor.
  * **Cuerpo recibido (JSON):** Datos modificados del proveedor.
  * **Resultado:** `200 OK` con la entidad `Proveedor` actualizada.

* **`DELETE /api/proveedores/{idProv}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idProv}` - ID del proveedor.
  * **Resultado:** `204 No Content` confirmando la eliminación.

---

### 6. Categorías (`/api/categorias`)
Clasificación de productos (ej: Calzado, Ropa deportiva, Accesorios).

* **`GET /api/categorias`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Resultado:** `200 OK` con `List<Categoria>`.

* **`GET /api/categorias/{idCategoria}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Parámetro:** `{idCategoria}` - ID de la categoría (ej: `CAT01`).
  * **Resultado:** `200 OK` con la entidad `Categoria`.

* **`POST /api/categorias`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Cuerpo recibido (JSON):** `{"nombre": "...", "descripcion": "..."}`
  * **Resultado:** `201 Created` con la nueva `Categoria`.

* **`PUT /api/categorias/{idCategoria}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idCategoria}` - ID de la categoría.
  * **Cuerpo recibido (JSON):** `{"nombre": "...", "descripcion": "..."}`
  * **Resultado:** `200 OK` con la entidad `Categoria` modificada.

* **`DELETE /api/categorias/{idCategoria}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idCategoria}` - ID de la categoría.
  * **Resultado:** `204 No Content` tras ser eliminada.

---

### 7. Almacenes (`/api/almacenes`)
Sedes, sucursales y espacios físicos de almacenamiento y venta.

* **`GET /api/almacenes`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Resultado:** `200 OK` con la lista de `Almacen`.

* **`GET /api/almacenes/{idAlma}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Parámetro:** `{idAlma}` - ID del almacén (ej: `ALM01`).
  * **Resultado:** `200 OK` con la entidad `Almacen`.

* **`POST /api/almacenes`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Cuerpo recibido (JSON):** `{"nombre": "...", "ubicacion": "...", "tipo": "TIENDA|DEPOSITO"}`
  * **Resultado:** `201 Created` con el `Almacen` creado.

* **`PUT /api/almacenes/{idAlma}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idAlma}` - ID del almacén.
  * **Cuerpo recibido (JSON):** `AlmacenRequestDTO` actualizado.
  * **Resultado:** `200 OK` con el `Almacen` actualizado.

* **`DELETE /api/almacenes/{idAlma}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idAlma}` - ID del almacén.
  * **Resultado:** `204 No Content` tras la eliminación.

---

### 8. Productos y Recomendaciones IA (`/api/productos`)
Catálogo principal de mercadería y motor de recomendación de productos similares.

* **`GET /api/productos`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Resultado:** `200 OK` con `List<Producto>` que contiene todos los productos (código, nombre, color, precio de compra/venta, stock total, categoría, proveedor e imagen).

* **`GET /api/productos/buscar-avanzado?categoria={idCategoria}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Parámetro Query:** `categoria` (ej: `CAT01`).
  * **Resultado:** `200 OK` con `List<Producto>` filtrados por la categoría especificada.

* **`GET /api/productos/{idProd}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Parámetro:** `{idProd}` - ID del producto (ej: `P001`).
  * **Resultado:** `200 OK` con la entidad `Producto`, o `404 Not Found` si no existe.

* **`POST /api/productos`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Cuerpo recibido (JSON):** `ProductoRequestDTO` (`codigo`, `nombre`, `color`, `precioCompra`, `precioVenta`, `stockMinimo`, `stockMaximo`, `idCategoria`, `idProv`, `imagenUrl`).
  * **Resultado:** `201 Created` con el nuevo `Producto`.

* **`PUT /api/productos/{idProd}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idProd}` - ID del producto a modificar.
  * **Cuerpo recibido (JSON):** `ProductoRequestDTO` con los datos a actualizar.
  * **Resultado:** `200 OK` con la entidad `Producto` actualizada.

* **`GET /api/productos/{id}/similares`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Parámetro:** `{id}` - ID del producto base (ej: `P001`).
  * **Resultado:** `200 OK` con `List<ProductoSimilarDTO>`, un listado de los 3 productos más similares ordenados por porcentaje de similitud (`similitud`, `id_prod`, `codigo`, `nombre`, `color`, `precio_venta`, `stock`, `imagen_url`). Consume el microservicio Python de Machine Learning en segundo plano.

---

### 9. Movimientos de Inventario (`/api/movimientos`)
Control de entradas, salidas y transferencias entre almacenes.

* **`GET /api/movimientos`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Resultado:** `200 OK` con `List<MovimientoResponseDTO>` listando los movimientos realizados con fecha, tipo y almacenes.

* **`GET /api/movimientos/{idMovi}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idMovi}` - ID del movimiento (ej: `MOV001`).
  * **Resultado:** `200 OK` con `MovimientoResponseDTO` con los ítems movidos, motivo y usuario que registró la operación.

* **`POST /api/movimientos`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Cuerpo recibido (JSON):** `{"tipo": "ENTRADA|SALIDA|TRASLADO", "idAlmaOrigen": "...", "idAlmaDestino": "...", "motivo": "...", "items": [{"idProd": "...", "cantidad": 10}]}`
  * **Resultado:** `201 Created` con `MovimientoResponseDTO` tras impactar directamente las existencias de inventario.

---

### 10. Pedidos a Proveedores (`/api/pedidos`)
Órdenes de compra y recepción física de mercancía al inventario.

* **`GET /api/pedidos`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Resultado:** `200 OK` con `List<PedidoResponseDTO>` con el listado de pedidos registrados.

* **`GET /api/pedidos/{idPedido}`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idPedido}` - ID del pedido (ej: `PED001`).
  * **Resultado:** `200 OK` con `PedidoResponseDTO` incluyendo los productos solicitados, cantidades, precios y estado.

* **`POST /api/pedidos`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Cuerpo recibido (JSON):** `{"idProv": "PRV001", "observaciones": "...", "items": [{"idProd": "...", "cantidad": 50, "precioUnitario": 120.00}]}`
  * **Resultado:** `201 Created` con `PedidoResponseDTO` en estado inicial `PENDIENTE`.

* **`POST /api/pedidos/{idPedido}/recibir`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idPedido}` - ID del pedido a ingresar.
  * **Cuerpo recibido (JSON):** `{"idAlma": "ALM01"}` (almacén de destino donde ingresa el lote).
  * **Resultado:** `200 OK` con `PedidoResponseDTO` en estado `RECIBIDO`, habiendo incrementado automáticamente el stock del almacén elegido.

* **`POST /api/pedidos/{idPedido}/cancelar`**
  * **Permiso:** `ADMIN`, `ALMACENERO`.
  * **Parámetro:** `{idPedido}` - ID del pedido.
  * **Resultado:** `200 OK` con `PedidoResponseDTO` actualizado a estado `CANCELADO`.

---

### 11. Ventas (`/api/ventas`)
Procesamiento de ventas a clientes y salida automática de stock.

* **`GET /api/ventas`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Resultado:** `200 OK` con `List<VentaResponseDTO>` con todas las transacciones de venta.

* **`GET /api/ventas/{idVenta}`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Parámetro:** `{idVenta}` - ID de la venta (ej: `VTA001`).
  * **Resultado:** `200 OK` con `VentaResponseDTO` detallando comprobante, cliente, vendedor, lista de productos y monto total.

* **`POST /api/ventas`**
  * **Permiso:** `ADMIN`, `VENDEDOR`.
  * **Cuerpo recibido (JSON):** `{"idCli": "CLI001", "idAlma": "ALM01", "tipoComprobante": "BOLETA|FACTURA", "nroComprobante": "B001-00123", "items": [{"idProd": "...", "cantidad": 2, "precioVenta": 299.90, "descuento": 0.0}]}`
  * **Resultado:** `201 Created` con `VentaResponseDTO` generado, reduciendo el stock disponible en el almacén de despacho.

---

### 12. Tareas del Personal (`/api/tareas`)
Asignación, seguimiento y control de actividades operativas.

* **`GET /api/tareas`**
  * **Permiso:** `ADMIN`, `ALMACENERO`, `VENDEDOR`.
  * **Resultado:** `200 OK` con `List<Tarea>`. Si el usuario autenticado es `ADMIN` devuelve todas las tareas; para los demás roles devuelve únicamente las tareas que tengan asignadas.

* **`GET /api/tareas/{idTarea}`**
  * **Permiso:** `ADMIN` o trabajador asignado.
  * **Parámetro:** `{idTarea}` - ID de la tarea (ej: `TAR001`).
  * **Resultado:** `200 OK` con la entidad `Tarea`.

* **`POST /api/tareas`**
  * **Permiso:** Solo `ADMIN`.
  * **Cuerpo recibido (JSON):** `{"idTrabAsignado": "TRAB02", "titulo": "...", "descripcion": "...", "prioridad": "ALTA|MEDIA|BAJA", "fechaLimite": "2026-12-01"}`
  * **Resultado:** `201 Created` con la `Tarea` creada con estado `PENDIENTE`.

* **`PUT /api/tareas/{idTarea}`**
  * **Permiso:** Solo `ADMIN`.
  * **Parámetro:** `{idTarea}` - ID de la tarea.
  * **Cuerpo recibido (JSON):** `TareaRequestDTO` con los datos actualizados.
  * **Resultado:** `200 OK` con la entidad `Tarea` modificada.

* **`PATCH /api/tareas/{idTarea}/estado`**
  * **Permiso:** `ADMIN` o el trabajador asignado a la tarea.
  * **Parámetro:** `{idTarea}` - ID de la tarea.
  * **Cuerpo recibido (JSON):** `{"estado": "PENDIENTE|EN_PROCESO|COMPLETADA|CANCELADA"}`
  * **Resultado:** `200 OK` con la `Tarea` reflejando el nuevo estado.

* **`DELETE /api/tareas/{idTarea}`**
  * **Permiso:** Solo `ADMIN`.
  * **Parámetro:** `{idTarea}` - ID de la tarea.
  * **Resultado:** `204 No Content` tras eliminar la tarea.

---

### 13. Documentación Interactiva (Swagger / OpenAPI)
Rutas habilitadas para exploración interactiva y pruebas de la API.

* **`GET /swagger-ui.html`** o **`GET /swagger-ui/index.html`**
  * **Permiso:** Público.
  * **Resultado:** `200 OK` (Página Web interactiva de Swagger UI para probar los endpoints en vivo).

* **`GET /v3/api-docs`**
  * **Permiso:** Público.
  * **Resultado:** `200 OK` (JSON con la especificación OpenAPI v3 de la API).

---

### 14. Microservicio de Recomendaciones IA (`adidas-recomendador` - Puerto 8000)
Servicio independiente desarrollado con FastAPI y Scikit-Learn que calcula la similitud de coseno y vectorización TF-IDF entre productos.

* **`GET http://localhost:8000/recomendar/{id_prod}?top_n=3`**
  * **Permiso:** Libre / Interno.
  * **Parámetros:**
    * `{id_prod}` (Path): Código del producto a comparar (ej: `P001`).
    * `top_n` (Query, opcional, por defecto 3): Cantidad de productos similares a retornar.
  * **Resultado:** `200 OK` con un objeto JSON con el producto base y el array de recomendaciones con su porcentaje de similitud:
    ```json
    {
      "producto_base": "P001",
      "recomendaciones": [
        {
          "id_prod": "P003",
          "codigo": "ART-003",
          "nombre": "Zapatilla Ultraboost Light",
          "color": "Negro",
          "precio_venta": 650.0,
          "stock": 18,
          "imagen_url": "https://...",
          "similitud": 88.5
        }
      ]
    }
    ```
  * En caso de no existir el producto, retorna `404 Not Found`.

* **`GET http://localhost:8000/docs`**
  * **Permiso:** Libre.
  * **Resultado:** `200 OK` (Swagger UI interactivo de FastAPI).

* **`GET http://localhost:8000/redoc`**
  * **Permiso:** Libre.
  * **Resultado:** `200 OK` (Documentación ReDoc de FastAPI).

* **`GET http://localhost:8000/openapi.json`**
  * **Permiso:** Libre.
  * **Resultado:** `200 OK` (JSON con el esquema OpenAPI del microservicio).
