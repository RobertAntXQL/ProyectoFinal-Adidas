package com.adidas.inventario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class ForecastResponseDTO {
    private String algoritmo;
    private String horizonte;

    @JsonProperty("periodo_objetivo")
    private String periodoObjetivo;

    @JsonProperty("metricas_evaluacion")
    private MetricasDTO metricasEvaluacion;

    private List<ItemPrediccionDTO> predicciones;

    public static class MetricasDTO {
        @JsonProperty("semanas_entrenamiento")
        private Integer semanasEntrenamiento;
        @JsonProperty("semanas_prueba")
        private Integer semanasPrueba;
        @JsonProperty("mae_pares")
        private BigDecimal maePares;
        @JsonProperty("rmse_pares")
        private BigDecimal rmsePares;

        public Integer getSemanasEntrenamiento() { return semanasEntrenamiento; }
        public void setSemanasEntrenamiento(Integer semanasEntrenamiento) { this.semanasEntrenamiento = semanasEntrenamiento; }
        public Integer getSemanasPrueba() { return semanasPrueba; }
        public void setSemanasPrueba(Integer semanasPrueba) { this.semanasPrueba = semanasPrueba; }
        public BigDecimal getMaePares() { return maePares; }
        public void setMaePares(BigDecimal maePares) { this.maePares = maePares; }
        public BigDecimal getRmsePares() { return rmsePares; }
        public void setRmsePares(BigDecimal rmsePares) { this.rmsePares = rmsePares; }
    }

    public static class ItemPrediccionDTO {
        @JsonProperty("id_prod")
        private String idProd;
        private String codigo;
        private String nombre;
        private String talla;
        @JsonProperty("stock_actual")
        private Integer stockActual;
        @JsonProperty("cantidad_estimada")
        private Integer cantidadEstimada;
        @JsonProperty("deficit_estimado")
        private Integer deficitEstimado;

        public String getIdProd() { return idProd; }
        public void setIdProd(String idProd) { this.idProd = idProd; }
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTalla() { return talla; }
        public void setTalla(String talla) { this.talla = talla; }
        public Integer getStockActual() { return stockActual; }
        public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
        public Integer getCantidadEstimada() { return cantidadEstimada; }
        public void setCantidadEstimada(Integer cantidadEstimada) { this.cantidadEstimada = cantidadEstimada; }
        public Integer getDeficitEstimado() { return deficitEstimado; }
        public void setDeficitEstimado(Integer deficitEstimado) { this.deficitEstimado = deficitEstimado; }
    }

    public String getAlgoritmo() { return algoritmo; }
    public void setAlgoritmo(String algoritmo) { this.algoritmo = algoritmo; }
    public String getHorizonte() { return horizonte; }
    public void setHorizonte(String horizonte) { this.horizonte = horizonte; }
    public String getPeriodoObjetivo() { return periodoObjetivo; }
    public void setPeriodoObjetivo(String periodoObjetivo) { this.periodoObjetivo = periodoObjetivo; }
    public MetricasDTO getMetricasEvaluacion() { return metricasEvaluacion; }
    public void setMetricasEvaluacion(MetricasDTO metricasEvaluacion) { this.metricasEvaluacion = metricasEvaluacion; }
    public List<ItemPrediccionDTO> getPredicciones() { return predicciones; }
    public void setPredicciones(List<ItemPrediccionDTO> predicciones) { this.predicciones = predicciones; }
}

