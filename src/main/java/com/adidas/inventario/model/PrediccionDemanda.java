package com.adidas.inventario.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "predicciones_demanda")
public class PrediccionDemanda {

    @Id
    @Column(name = "id_prediccion", length = 36)
    private String idPrediccion;

    @Column(name = "id_prod", length = 36, nullable = false)
    private String idProd;

    @Column(name = "horizonte", length = 20, nullable = false)
    private String horizonte;

    @Column(name = "periodo_objetivo", length = 40, nullable = false)
    private String periodoObjetivo;

    @Column(name = "cantidad_estimada", nullable = false)
    private Integer cantidadEstimada;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "deficit_estimado", nullable = false)
    private Integer deficitEstimado;

    @Column(name = "mae_modelo", precision = 10, scale = 2)
    private BigDecimal maeModelo;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    public String getIdPrediccion() { return idPrediccion; }
    public void setIdPrediccion(String idPrediccion) { this.idPrediccion = idPrediccion; }
    public String getIdProd() { return idProd; }
    public void setIdProd(String idProd) { this.idProd = idProd; }
    public String getHorizonte() { return horizonte; }
    public void setHorizonte(String horizonte) { this.horizonte = horizonte; }
    public String getPeriodoObjetivo() { return periodoObjetivo; }
    public void setPeriodoObjetivo(String periodoObjetivo) { this.periodoObjetivo = periodoObjetivo; }
    public Integer getCantidadEstimada() { return cantidadEstimada; }
    public void setCantidadEstimada(Integer cantidadEstimada) { this.cantidadEstimada = cantidadEstimada; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public Integer getDeficitEstimado() { return deficitEstimado; }
    public void setDeficitEstimado(Integer deficitEstimado) { this.deficitEstimado = deficitEstimado; }
    public BigDecimal getMaeModelo() { return maeModelo; }
    public void setMaeModelo(BigDecimal maeModelo) { this.maeModelo = maeModelo; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
}

