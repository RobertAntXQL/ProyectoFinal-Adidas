from collections import defaultdict
from datetime import datetime, timedelta
import numpy as np
import pymysql
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, mean_squared_error


def obtener_historial_semanal(db_config: dict) -> list[dict]:
    """Agrupa las salidas semanales directamente desde MySQL."""
    query = """
        SELECT 
            p.id_prod,
            p.codigo,
            p.nombre,
            p.talla,
            p.precio_venta,
            p.stock,
            YEARWEEK(salidas.fecha, 1) AS anio_semana,
            MONTH(MIN(salidas.fecha)) AS mes,
            SUM(salidas.cantidad) AS unidades_vendidas
        FROM productos p
        INNER JOIN (
            SELECT v.fecha, vd.id_prod, vd.cantidad
            FROM ventas v
            INNER JOIN ventas_detalle vd ON v.id_venta = vd.id_venta
            WHERE v.estado_pedido <> 'Anulado'
            UNION ALL
            SELECT m.fecha, md.id_produc AS id_prod, md.cantidad
            FROM movimientos m
            INNER JOIN movimientos_detalle md ON m.id_movi = md.id_movi
            WHERE m.tipo = 'Salida'
        ) salidas ON p.id_prod = salidas.id_prod
        WHERE p.id_categoria = 'C001'
        GROUP BY p.id_prod, p.codigo, p.nombre, p.talla, p.precio_venta, p.stock, YEARWEEK(salidas.fecha, 1)
        ORDER BY anio_semana ASC, p.id_prod ASC
    """
    conexion = pymysql.connect(**db_config)
    try:
        with conexion.cursor() as cursor:
            cursor.execute(query)
            return list(cursor.fetchall())
    finally:
        conexion.close()


def entrenar_y_predecir(db_config: dict, horizonte: str = "SEMANAL") -> dict:
    filas = obtener_historial_semanal(db_config)
    if not filas:
        raise ValueError("No hay historial de salidas en MySQL. Ejecuta sp_poblar_historial_ventas().")

    productos_unicos = sorted({f["id_prod"] for f in filas})
    mapa_prod = {pid: float(idx) for idx, pid in enumerate(productos_unicos)}

    semanas_ordenadas = sorted({f["anio_semana"] for f in filas})
    mapa_semana = {sem: float(idx) for idx, sem in enumerate(semanas_ordenadas)}

    por_producto: dict[str, list[dict]] = defaultdict(list)
    for f in filas:
        por_producto[f["id_prod"]].append(f)

    X_list, y_list, t_list = [], [], []

    for pid in productos_unicos:
        serie = sorted(por_producto[pid], key=lambda x: mapa_semana[x["anio_semana"]])
        ventas = [float(r["unidades_vendidas"]) for r in serie]

        for i in range(4, len(serie)):
            fila = serie[i]
            try:
                talla_num = float(fila["talla"]) if fila["talla"] is not None else 40.0
            except ValueError:
                talla_num = 40.0

            t_val = mapa_semana[fila["anio_semana"]]
            X_list.append([
                mapa_prod[pid],
                talla_num,
                float(fila["precio_venta"]),
                t_val,
                float(fila["mes"]),
                ventas[i - 1],
                ventas[i - 2],
                ventas[i - 4],
                float(np.mean(ventas[i - 4:i])),
            ])
            y_list.append(ventas[i])
            t_list.append(t_val)

    if not X_list:
        raise ValueError("Se necesitan al menos 5 semanas de historial por producto.")

    X = np.array(X_list, dtype=np.float64)
    y = np.array(y_list, dtype=np.float64)
    t_arr = np.array(t_list, dtype=np.float64)

    # Separación temporal: Pasado (Train) vs Últimas 4 semanas (Test)
    semanas_prueba = 4
    t_corte = t_arr.max() - semanas_prueba
    mask_train = t_arr <= t_corte
    mask_test = t_arr > t_corte

    rf_eval = RandomForestRegressor(n_estimators=100, max_depth=8, random_state=42, n_jobs=1)
    rf_eval.fit(X[mask_train], y[mask_train])
    pred_test = rf_eval.predict(X[mask_test])

    mae = round(float(mean_absolute_error(y[mask_test], pred_test)), 2)
    rmse = round(float(np.sqrt(mean_squared_error(y[mask_test], pred_test))), 2)

    # Entrenamiento final con todo el historial
    rf_final = RandomForestRegressor(n_estimators=100, max_depth=8, random_state=42, n_jobs=1)
    rf_final.fit(X, y)

    pasos = 4 if horizonte.upper() == "MENSUAL" else 1
    hoy = datetime.now()
    periodo_objetivo = (
        f"Próximo Mes ({(hoy + timedelta(days=30)).strftime('%Y-%m')})"
        if pasos == 4
        else f"Próxima Semana (W{(hoy + timedelta(days=7)).isocalendar()[1]})"
    )

    resultados = []
    for pid in productos_unicos:
        serie = sorted(por_producto[pid], key=lambda x: mapa_semana[x["anio_semana"]])
        ultima = serie[-1]
        ultimas_ventas = [float(r["unidades_vendidas"]) for r in serie[-4:]]

        try:
            talla_num = float(ultima["talla"]) if ultima["talla"] is not None else 40.0
        except ValueError:
            talla_num = 40.0

        acumulado_predicho = 0
        t_futuro = mapa_semana[ultima["anio_semana"]]

        for paso in range(1, pasos + 1):
            t_futuro += 1.0
            mes_futuro = float((hoy + timedelta(days=7 * paso)).month)
            fila_np = np.array([[
                mapa_prod[pid],
                talla_num,
                float(ultima["precio_venta"]),
                t_futuro,
                mes_futuro,
                ultimas_ventas[-1],
                ultimas_ventas[-2],
                ultimas_ventas[-4],
                float(np.mean(ultimas_ventas[-4:])),
            ]], dtype=np.float64)

            pred_paso = max(1, int(round(rf_final.predict(fila_np)[0])))
            acumulado_predicho += pred_paso
            ultimas_ventas.append(float(pred_paso))

        stock_actual = int(ultima["stock"])
        resultados.append({
            "id_prod": pid,
            "codigo": ultima["codigo"],
            "nombre": ultima["nombre"],
            "talla": ultima["talla"],
            "stock_actual": stock_actual,
            "cantidad_estimada": acumulado_predicho,
            "deficit_estimado": max(0, acumulado_predicho - stock_actual),
        })

    resultados.sort(key=lambda x: x["cantidad_estimada"], reverse=True)

    return {
        "algoritmo": "RandomForestRegressor (100 árboles)",
        "horizonte": horizonte.upper(),
        "periodo_objetivo": periodo_objetivo,
        "metricas_evaluacion": {
            "semanas_entrenamiento": int(len(np.unique(t_arr[mask_train]))),
            "semanas_prueba": int(len(np.unique(t_arr[mask_test]))),
            "mae_pares": mae,
            "rmse_pares": rmse,
        },
        "predicciones": resultados,
    }
