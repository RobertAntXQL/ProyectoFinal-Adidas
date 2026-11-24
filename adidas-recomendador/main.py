import os
import pandas as pd
import numpy as np
import pymysql
from fastapi import FastAPI, HTTPException
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics.pairwise import cosine_similarity

app = FastAPI(title="Adidas Recomendador API", version="1.0")

# Configuración de conexión a tu contenedor Docker MySQL (puerto 3307)
DB_CONFIG = {
    "host": "127.0.0.1",
    "port": 3307,
    "user": "adidas_app",
    "password": os.getenv("ADIDAS_DB_PASSWORD", "AdidasDB2026!"),
    "database": "adidas_app25",
    "charset": "utf8mb4",
    "cursorclass": pymysql.cursors.DictCursor,
}


def obtener_productos() -> pd.DataFrame:
    """Lee el catálogo actual de MySQL sin modificar ninguna tabla."""
    query = """
        SELECT 
            p.id_prod,
            p.codigo,
            p.nombre,
            p.color,
            p.precio_venta,
            p.stock,
            p.imagen_url,
            c.nombre AS categoria
        FROM productos p
        INNER JOIN categorias c ON p.id_categoria = c.id_categoria
    """
    conexion = pymysql.connect(**DB_CONFIG)
    try:
        with conexion.cursor() as cursor:
            cursor.execute(query)
            filas = cursor.fetchall()
        return pd.DataFrame(filas)
    finally:
        conexion.close()


@app.get("/recomendar/{id_prod}")
def recomendar_similares(id_prod: str, top_n: int = 3):
    df = obtener_productos()

    if df.empty or id_prod not in df["id_prod"].values:
        raise HTTPException(
            status_code=404,
            detail=f"El producto '{id_prod}' no existe en el catálogo."
        )

    # Limpieza de tipos: Decimal a float y nulos (como imagen_url en P010-P013) a None
    df["precio_venta"] = df["precio_venta"].astype(float)
    df["color"] = df["color"].fillna("")
    df = df.where(pd.notnull(df), None)

    # --- INICIO DEL MODELO DE MACHINE LEARNING (~15 líneas) ---
    # 1. Combinamos texto relevante: categoría + nombre + color
    df["texto_caracteristicas"] = (
        df["categoria"] + " " + df["nombre"] + " " + df["color"]
    )

    # 2. Vectorización TF-IDF para texto y escala [0, 1] para el precio
    vectorizador = TfidfVectorizer()
    matriz_texto = vectorizador.fit_transform(df["texto_caracteristicas"]).toarray()

    escalador = MinMaxScaler()
    matriz_precio = escalador.fit_transform(df[["precio_venta"]])

    # 3. Unimos texto + precio en un solo vector por producto y calculamos similitud
    matriz_final = np.hstack([matriz_texto, matriz_precio])
    similitudes = cosine_similarity(matriz_final)
    # --- FIN DEL MODELO ---

    # Ubicamos el índice del producto consultado y ordenamos sus vecinos más cercanos
    idx_objetivo = df.index[df["id_prod"] == id_prod][0]
    puntajes = list(enumerate(similitudes[idx_objetivo]))
    puntajes_ordenados = sorted(puntajes, key=lambda x: x[1], reverse=True)

    # Filtramos el propio producto (índice 0) y tomamos los Top 3
    recomendaciones = []
    for idx, score in puntajes_ordenados:
        if idx == idx_objetivo:
            continue
        fila = df.iloc[idx]
        recomendaciones.append({
            "id_prod": fila["id_prod"],
            "codigo": fila["codigo"],
            "nombre": fila["nombre"],
            "color": fila["color"],
            "precio_venta": fila["precio_venta"],
            "stock": int(fila["stock"]),
            "imagen_url": fila["imagen_url"],
            "similitud": round(float(score) * 100, 1)  # Porcentaje legible (ej. 84.5%)
        })
        if len(recomendaciones) == top_n:
            break

    return {
        "producto_base": id_prod,
        "recomendaciones": recomendaciones
    }