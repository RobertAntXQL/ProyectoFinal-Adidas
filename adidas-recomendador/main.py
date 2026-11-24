import os
os.environ["OMP_NUM_THREADS"] = "1"
os.environ["OPENBLAS_NUM_THREADS"] = "1"
os.environ["MKL_NUM_THREADS"] = "1"

import numpy as np
import pandas as pd
import pymysql
from fastapi import FastAPI, HTTPException
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import MinMaxScaler

from forecasting import entrenar_y_predecir

app = FastAPI(title="Adidas ML API", version="1.0")

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
async def recomendar_similares(id_prod: str, top_n: int = 3):
    df = obtener_productos()

    if df.empty or id_prod not in df["id_prod"].values:
        raise HTTPException(
            status_code=404,
            detail=f"El producto '{id_prod}' no existe en el catálogo."
        )

    df = df.assign(
        precio_venta=df["precio_venta"].astype(float),
        color=df["color"].fillna(""),
    )
    df = df.where(pd.notnull(df), None)

    df["texto_caracteristicas"] = (
        df["categoria"] + " " + df["nombre"] + " " + df["color"]
    )

    vectorizador = TfidfVectorizer()
    matriz_texto = vectorizador.fit_transform(df["texto_caracteristicas"]).toarray()

    escalador = MinMaxScaler()
    matriz_precio = escalador.fit_transform(df[["precio_venta"]])

    matriz_final = np.hstack([matriz_texto, matriz_precio])
    similitudes = cosine_similarity(matriz_final)

    idx_objetivo = df.index[df["id_prod"] == id_prod][0]
    puntajes = list(enumerate(similitudes[idx_objetivo]))
    puntajes_ordenados = sorted(puntajes, key=lambda x: x[1], reverse=True)

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
            "similitud": round(float(score) * 100, 1)
        })
        if len(recomendaciones) == top_n:
            break

    return {
        "producto_base": id_prod,
        "recomendaciones": recomendaciones
    }


@app.get("/predecir")
async def predecir_demanda(horizonte: str = "SEMANAL"):
    try:
        return entrenar_y_predecir(DB_CONFIG, horizonte=horizonte)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
