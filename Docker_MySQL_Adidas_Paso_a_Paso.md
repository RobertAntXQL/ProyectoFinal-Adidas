# ADIDAS BRAND CENTER | MYSQL EN DOCKER

1. Crear el contenedor (terminal de Ubuntu, instalación inicial)

docker run -d \
  --name mi-mysql-adidas \
  --restart unless-stopped \
  -e MYSQL_ROOT_PASSWORD=AD777o \
  -e MYSQL_DATABASE=adidas_app25 \
  -e MYSQL_USER=adidas_app \
  -e 'MYSQL_PASSWORD=AdidasDB2026!' \
  -p 127.0.0.1:3307:3306 \
  -v mi-mysql-adidas-datos:/var/lib/mysql \
  mysql:8.4 \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_0900_ai_ci \
  --default-time-zone=-05:00

> docker logs --tail 40 mi-mysql-adidas
## MySQL está listo cuando aparece «ready for connections» con el puerto 3306.

2. Entrar como root (terminal de Ubuntu)

> Contraseña de root al aparecer Enter password = AD777o
docker exec -it mi-mysql-adidas mysql -u root -p

3. Crear la base y las tablas con datos de ejemplo (pegar en mysql>, instalación inicial)

SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS adidas_app25
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE adidas_app25;

-- Organización y personal

CREATE TABLE almacenes (
    id_alma VARCHAR(36) PRIMARY KEY,
    letra_zona CHAR(1) NOT NULL,
    descripcion VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE roles (
    id_rol VARCHAR(36) PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE trabajadores (
    id_trab VARCHAR(36) PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    numero_celu VARCHAR(20),
    direccion VARCHAR(200),
    id_rol VARCHAR(36) NOT NULL,
    CONSTRAINT fk_trabajadores_roles
        FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
) ENGINE=InnoDB;

CREATE TABLE usuarios (
    id_usu VARCHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
    id_trab VARCHAR(36) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
    CONSTRAINT fk_usuarios_trabajadores
        FOREIGN KEY (id_trab) REFERENCES trabajadores(id_trab)
) ENGINE=InnoDB;

-- Proveedores y clientes

CREATE TABLE proveedores (
    id_prov VARCHAR(36) PRIMARY KEY,
    ruc_dni VARCHAR(20) NOT NULL UNIQUE,
    razon_social VARCHAR(150) NOT NULL,
    contacto VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    ciudad VARCHAR(100),
    pais VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE clientes (
    id_cli VARCHAR(36) PRIMARY KEY,
    dni VARCHAR(20) UNIQUE,
    nombre VARCHAR(150),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    ciudad VARCHAR(100)
) ENGINE=InnoDB;

-- Catálogo e inventario

CREATE TABLE categorias (
    id_categoria VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    tipo_talla VARCHAR(20) NOT NULL,
    CONSTRAINT ck_categorias_tipo_talla
        CHECK (tipo_talla IN ('NUMERICA', 'LETRAS', 'SIN_TALLA'))
) ENGINE=InnoDB;

CREATE TABLE productos (
    id_prod VARCHAR(36) PRIMARY KEY,
    id_categoria VARCHAR(36) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    talla VARCHAR(20),
    color VARCHAR(50),
    precio_compra DECIMAL(10,2),
    precio_venta DECIMAL(10,2),
    stock INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 8,
    imagen_url VARCHAR(500),
    CONSTRAINT fk_productos_categorias
        FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
    CONSTRAINT ck_productos_stock CHECK (stock >= 0),
    CONSTRAINT ck_productos_stock_minimo CHECK (stock_minimo >= 0),
    CONSTRAINT ck_productos_precio_compra CHECK (precio_compra >= 0),
    CONSTRAINT ck_productos_precio_venta CHECK (precio_venta >= 0),
    CONSTRAINT ck_productos_talla CHECK (talla IS NULL OR CHAR_LENGTH(TRIM(talla)) > 0)
) ENGINE=InnoDB;

-- Ventas y detalles

CREATE TABLE ventas (
    id_venta VARCHAR(36) PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cli VARCHAR(36),
    id_usu VARCHAR(36) NOT NULL,
    direccion_entrega VARCHAR(200),
    distrito_entrega VARCHAR(100),
    referencia_entrega VARCHAR(200),
    forma_pago VARCHAR(50),
    estado_pedido VARCHAR(50) NOT NULL DEFAULT 'Confirmado',
    CONSTRAINT fk_ventas_clientes
        FOREIGN KEY (id_cli) REFERENCES clientes(id_cli),
    CONSTRAINT fk_ventas_usuarios
        FOREIGN KEY (id_usu) REFERENCES usuarios(id_usu)
) ENGINE=InnoDB;

CREATE TABLE ventas_detalle (
    id_detalle VARCHAR(36) PRIMARY KEY,
    id_venta VARCHAR(36) NOT NULL,
    id_prod VARCHAR(36) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_ventas_detalle_ventas
        FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    CONSTRAINT fk_ventas_detalle_productos
        FOREIGN KEY (id_prod) REFERENCES productos(id_prod),
    CONSTRAINT ck_ventas_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_ventas_detalle_precio CHECK (precio_unitario >= 0)
) ENGINE=InnoDB;

-- Movimientos de inventario

CREATE TABLE movimientos (
    id_movi VARCHAR(36) PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(10) NOT NULL,
    id_usuario VARCHAR(36) NOT NULL,
    id_alma VARCHAR(36) NOT NULL,
    id_prov VARCHAR(36),
    motivo VARCHAR(200) NOT NULL,
    CONSTRAINT fk_movimientos_usuarios
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usu),
    CONSTRAINT fk_movimientos_almacenes
        FOREIGN KEY (id_alma) REFERENCES almacenes(id_alma),
    CONSTRAINT fk_movimientos_proveedores
        FOREIGN KEY (id_prov) REFERENCES proveedores(id_prov),
    CONSTRAINT ck_movimientos_tipo CHECK (tipo IN ('Entrada', 'Salida')),
    CONSTRAINT ck_movimientos_motivo CHECK (CHAR_LENGTH(TRIM(motivo)) > 0)
) ENGINE=InnoDB;

CREATE TABLE movimientos_detalle (
    id_mov_det VARCHAR(36) PRIMARY KEY,
    id_movi VARCHAR(36) NOT NULL,
    id_produc VARCHAR(36) NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2),
    CONSTRAINT fk_movimientos_detalle_movimientos
        FOREIGN KEY (id_movi) REFERENCES movimientos(id_movi),
    CONSTRAINT fk_movimientos_detalle_productos
        FOREIGN KEY (id_produc) REFERENCES productos(id_prod),
    CONSTRAINT ck_movimientos_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_movimientos_detalle_precio CHECK (precio >= 0)
) ENGINE=InnoDB;

-- Pedidos a proveedores

CREATE TABLE pedidos_proveedor (
    id_pedido VARCHAR(36) PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_prov VARCHAR(36) NOT NULL,
    id_usu VARCHAR(36) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'Pendiente',
    observacion VARCHAR(200),
    CONSTRAINT fk_pedidos_proveedor_proveedores
        FOREIGN KEY (id_prov) REFERENCES proveedores(id_prov),
    CONSTRAINT fk_pedidos_proveedor_usuarios
        FOREIGN KEY (id_usu) REFERENCES usuarios(id_usu)
) ENGINE=InnoDB;

CREATE TABLE pedidos_proveedor_detalle (
    id_ped_det VARCHAR(36) PRIMARY KEY,
    id_pedido VARCHAR(36) NOT NULL,
    id_prod VARCHAR(36) NOT NULL,
    cantidad INT NOT NULL,
    precio_compra DECIMAL(10,2),
    CONSTRAINT fk_pedidos_proveedor_detalle_pedidos
        FOREIGN KEY (id_pedido) REFERENCES pedidos_proveedor(id_pedido),
    CONSTRAINT fk_pedidos_proveedor_detalle_productos
        FOREIGN KEY (id_prod) REFERENCES productos(id_prod),
    CONSTRAINT ck_pedidos_proveedor_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_pedidos_proveedor_detalle_precio CHECK (precio_compra >= 0)
) ENGINE=InnoDB;

-- Datos de ejemplo

START TRANSACTION;

INSERT INTO roles (id_rol, nombre_rol) VALUES
('R001', 'Administrador'),
('R002', 'Almacenero'),
('R003', 'Vendedor');

INSERT INTO trabajadores
(id_trab, dni, nombre, apellido, fecha_nacimiento, numero_celu, direccion, id_rol)
VALUES ('T001', '77777777', 'Carlos', 'Mendoza', '1995-08-15',
        '987654321', 'Av. Las Flores 123, Lima', 'R001');

INSERT INTO usuarios (id_trab, username, password)
VALUES ('T001', 'admin', '$2b$10$iJwO8Qb59YeykgN6x8YkgudU1pX.F2vo.U.7Wz1n.tkdcdXcwjhDO');

INSERT INTO almacenes (id_alma, letra_zona, descripcion) VALUES
('A001', 'A', 'Almacen principal Adidas Brand Center');

INSERT INTO proveedores
(id_prov, ruc_dni, razon_social, contacto, telefono, email, direccion, ciudad, pais)
VALUES
('PROV001', '20123456789', 'Adidas Peru SAC', 'Area de abastecimiento',
 '987654321', 'proveedores@adidas.com', 'Av. Principal 123', 'Lima', 'Peru'),
('PROV002', '20987654321', 'Distribuidora Deportiva Global', 'Carlos Ramirez',
 '955444333', 'contacto@deportivaglobal.com', 'Jr. Comercio 456', 'Lima', 'Peru');

INSERT INTO categorias (id_categoria, nombre, tipo_talla) VALUES
('C001', 'Calzado', 'NUMERICA'),
('C002', 'Medias', 'LETRAS'),
('C003', 'Mochilas', 'SIN_TALLA');

INSERT INTO productos
(id_prod, id_categoria, codigo, nombre, talla, color,
 precio_compra, precio_venta, stock, imagen_url)
VALUES
('P001', 'C001', 'SAMBA-OG-01', 'ZAPATILLAS SAMBA OG', '41', 'Negro/Blanco', 50.00, 90.00, 15,
 'https://assets.adidas.com/images/w_500,f_auto,q_auto/3bbecbdf584e40398446a8bf0117cf62_9366/Zapatillas_Samba_OG_Blanco_B75806_01_00_standard.jpg'),

('P002', 'C001', 'ADIZERO-EVO-SL', 'ZAPATILLAS ADIZERO EVO SL', '42', 'Blanco/Negro', 70.00, 120.00, 10,
 'https://assets.adidas.com/images/w_600,f_auto,q_auto/b8d2e6f0cdd940f6bb2988cb68ee5bf0_9366/Zapatillas_Adizero_EVO_SL_Negro_JP7149_HM1.jpg'),

('P003', 'C001', 'SL72-OG-01', 'ZAPATILLAS SL72 OG', '40', 'Guinda/Oro', 45.00, 85.00, 8,
 'https://assets.adidas.com/images/w_500,f_auto,q_auto/e7d955c4f1fa4f179a4e8308ea7ca8fc_9366/Zapatillas_SL_72_RS_Azul_IG2132_01_standard.jpg'),

('P004', 'C001', 'TECHNO-2000', 'ZAPATILLAS TECHNOCHAOS 2000', '43', 'Gris/Plata', 60.00, 110.00, 5,
 'https://assets.adidas.com/images/e_trim:EAEEEF/c_lpad,w_iw,h_ih/b_rgb:EAEEEF/w_180,f_auto,q_auto,fl_lossy,c_fill,g_auto/dc955b2f2f254bf48823a4b2bff1406a_9366/ZAPATILLAS_TECHNOCHAOS_2000_Negro_JR7228_HM1.jpg'),

('P005', 'C001', 'SUPERSTAR-01', 'ZAPATILLAS SUPERSTAR', '42', 'Blanco/Negro', 40.00, 80.00, 20,
 'https://assets.adidas.com/images/w_500,f_auto,q_auto/1a6c516af64c4832829354533392a713_9366/Zapatillas_Superstar_Negro_JI0079_01_standard.jpg'),

('P006', 'C001', 'GAZELLE-OG', 'ZAPATILLAS GAZELLE', '41', 'Azul/Blanco', 45.00, 85.00, 12,
 'https://assets.adidas.com/images/c_fill,g_auto,w_1200,h_630,f_auto,q_auto/w_600,f_auto,q_auto/242d09cebfa44d02a829a89fa808b6cb_9366/Zapatillas_Gazelle_Indoor_Verde_JI2062_01_standard.jpg'),

('P007', 'C001', 'FORUM-LOW-01', 'ZAPATILLAS FORUM LOW', '43', 'Blanco/Azul', 55.00, 100.00, 7,
 'https://assets.adidas.com/images/w_600,f_auto,q_auto/445dbb48d6fa4ea090f24ec0acd017ac_9366/Zapatillas_Forum_Low_CL_Verde_JQ2656_01_00_standard.jpg'),

('P008', 'C001', 'CAMPUS-00S', 'ZAPATILLAS CAMPUS 00S', '40', 'Verde/Blanco', 50.00, 95.00, 14,
 'https://assets.adidas.com/images/w_600,f_auto,q_auto/8943df93a9e94f81adb7af63011856c7_9366/Zapatillas_Campus_00s_Negro_HQ6638_01_standard.jpg'),

('P009', 'C001', 'ULTRABOOST-5', 'ZAPATILLAS ULTRABOOST 5', '42', 'White', 90.00, 160.00, 9,
 'https://assets.adidas.com/images/w_600,f_auto,q_auto/8d1a3eeadd184aeea391a4859f7905c4_9366/Zapatillas_Ultraboost_5_Blanco_ID8848_HM1.jpg');

INSERT INTO productos
(id_prod, id_categoria, codigo, nombre, talla, color,
 precio_compra, precio_venta, stock, imagen_url)
VALUES
('P010', 'C002', 'MEDIAS-CREW-S', 'MEDIAS CREW', 'S', 'Blanco', 12.00, 25.00, 12, NULL),
('P011', 'C002', 'MEDIAS-CREW-M', 'MEDIAS CREW', 'M', 'Blanco', 12.00, 25.00, 18, NULL),
('P012', 'C002', 'MEDIAS-CREW-L', 'MEDIAS CREW', 'L', 'Blanco', 12.00, 25.00, 10, NULL),
('P013', 'C003', 'MOCHILA-CLASSIC-01', 'MOCHILA CLASSIC', NULL, 'Negro', 45.00, 89.90, 12, NULL);

COMMIT;

-- Tareas
CREATE TABLE IF NOT EXISTS tareas (
    id_tarea VARCHAR(36) PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL,
    descripcion VARCHAR(400),
    estado VARCHAR(20) NOT NULL,
    fecha DATETIME NOT NULL,
    id_usuario VARCHAR(36) NOT NULL,
    CONSTRAINT fk_tareas_usuarios FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usu),
    CONSTRAINT ck_tareas_estado CHECK (estado IN ('Pendiente', 'En progreso', 'Completada'))
) ENGINE=InnoDB;

-- =====================================================================
-- Predicción de Demanda (Machine Learning - Random Forest)
-- =====================================================================
-- ¿Para qué sirve esta sección?
-- 1. Tabla 'predicciones_demanda': Es obligatoria para que Spring Boot arranque sin
--    errores al tener configurado 'spring.jpa.hibernate.ddl-auto=validate'. Almacena
--    las estimaciones proyectadas (horizonte semanal/mensual, cantidad estimada,
--    stock actual, déficit calculado y error MAE del modelo).
-- 2. Procedimiento 'sp_poblar_historial_ventas': Puebla 8 meses (34 semanas) de
--    ventas históricas para las 9 zapatillas del catálogo (P001 a P009). El algoritmo
--    Random Forest requiere este historial para construir las características temporales
--    (lags 1, 2 y 4 semanas atrás, y media móvil) y realizar el entrenamiento.
-- =====================================================================

USE adidas_app25;

-- 1. Tabla para guardar las predicciones (compatible con ddl-auto=validate)
CREATE TABLE IF NOT EXISTS predicciones_demanda (
    id_prediccion VARCHAR(36) PRIMARY KEY,
    id_prod VARCHAR(36) NOT NULL,
    horizonte VARCHAR(20) NOT NULL,
    periodo_objetivo VARCHAR(40) NOT NULL,
    cantidad_estimada INT NOT NULL,
    stock_actual INT NOT NULL,
    deficit_estimado INT NOT NULL,
    mae_modelo DECIMAL(10,2),
    fecha_generacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_predicciones_productos
        FOREIGN KEY (id_prod) REFERENCES productos(id_prod)
) ENGINE=InnoDB;

-- 2. Historial de 8 meses de ventas (ejecutar una sola vez si aún no lo poblaste)
DROP PROCEDURE IF EXISTS sp_poblar_historial_ventas;

DELIMITER //
CREATE PROCEDURE sp_poblar_historial_ventas()
BEGIN
    DECLARE v_id_usu VARCHAR(36);
    DECLARE v_semana INT DEFAULT 34;
    DECLARE v_fecha DATETIME;
    DECLARE v_id_venta VARCHAR(36);
    DECLARE v_prod_num INT;
    DECLARE v_id_prod VARCHAR(36);
    DECLARE v_precio DECIMAL(10,2);
    DECLARE v_cantidad INT;

    SELECT id_usu INTO v_id_usu FROM usuarios LIMIT 1;

    WHILE v_semana >= 1 DO
        SET v_fecha = DATE_SUB(NOW(), INTERVAL v_semana WEEK);
        SET v_id_venta = UUID();

        INSERT INTO ventas (id_venta, fecha, id_usu, forma_pago, estado_pedido)
        VALUES (v_id_venta, v_fecha, v_id_usu, 'Tarjeta', 'Confirmado');

        SET v_prod_num = 1;
        WHILE v_prod_num <= 9 DO
            SET v_id_prod = CONCAT('P00', v_prod_num);
            SELECT precio_venta INTO v_precio FROM productos WHERE id_prod = v_id_prod;

            SET v_cantidad = GREATEST(1,
                (CASE v_prod_num
                    WHEN 1 THEN 7
                    WHEN 5 THEN 6
                    WHEN 6 THEN 5
                    WHEN 8 THEN 5
                    WHEN 2 THEN 4
                    WHEN 4 THEN 4
                    WHEN 7 THEN 3
                    WHEN 3 THEN 3
                    ELSE 2
                END)
                + (CASE WHEN MOD(v_semana, 4) = 0 THEN 2 ELSE 0 END)
                + FLOOR(RAND() * 3) - 1
            );

            INSERT INTO ventas_detalle (id_detalle, id_venta, id_prod, cantidad, precio_unitario)
            VALUES (UUID(), v_id_venta, v_id_prod, v_cantidad, v_precio);

            SET v_prod_num = v_prod_num + 1;
        END WHILE;

        SET v_semana = v_semana - 1;
    END WHILE;
END //
DELIMITER ;

-- Ejecuta CALL solo si aún no tienes ventas históricas cargadas:
-- CALL sp_poblar_historial_ventas();
DROP PROCEDURE IF EXISTS sp_poblar_historial_ventas;

exit;

4. Iniciar y comprobar la base (terminal de Ubuntu, uso diario)

docker start mi-mysql-adidas
docker ps --filter name=mi-mysql-adidas

# Contraseña de adidas_app al aparecer Enter password = AdidasDB2026!
docker exec -it mi-mysql-adidas mysql -u adidas_app -p -D adidas_app25 -e 'SHOW TABLES;'

5. Conectar Spring Tools

src/main/resources/application.properties

spring.application.name=adidas-inventario
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3307/adidas_app25?sslMode=DISABLED&allowPublicKeyRetrieval=true&connectionTimeZone=America/Lima
spring.datasource.username=adidas_app
spring.datasource.password=${ADIDAS_DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false

Run Configurations > Spring Boot App > adidas-inventario > Environment > New

Name   ADIDAS_DB_PASSWORD
Value  AdidasDB2026!
