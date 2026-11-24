package com.adidas.inventario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class RecomendacionDTO {

    @JsonProperty("producto_base")
    private String productoBase;

    private List<ProductoSimilarDTO> recomendaciones;

    public String getProductoBase() { return productoBase; }
    public void setProductoBase(String productoBase) { this.productoBase = productoBase; }
    public List<ProductoSimilarDTO> getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(List<ProductoSimilarDTO> recomendaciones) { this.recomendaciones = recomendaciones; }

    public static class ProductoSimilarDTO {
        @JsonProperty("id_prod")
        private String idProd;
        private String codigo;
        private String nombre;
        private String color;

        @JsonProperty("precio_venta")
        private BigDecimal precioVenta;
        private Integer stock;

        @JsonProperty("imagen_url")
        private String imagenUrl;
        private Double similitud;

        public String getIdProd() { return idProd; }
        public void setIdProd(String idProd) { this.idProd = idProd; }
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public BigDecimal getPrecioVenta() { return precioVenta; }
        public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
        public Double getSimilitud() { return similitud; }
        public void setSimilitud(Double similitud) { this.similitud = similitud; }
    }
}