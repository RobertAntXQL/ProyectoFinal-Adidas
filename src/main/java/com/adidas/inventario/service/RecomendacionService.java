package com.adidas.inventario.service;

import com.adidas.inventario.dto.RecomendacionDTO;
import com.adidas.inventario.dto.RecomendacionDTO.ProductoSimilarDTO;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class RecomendacionService {

    private final RestTemplate restTemplate;
    private static final String PYTHON_API_URL = "http://localhost:8000/recomendar/";

    public RecomendacionService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(4000);
        factory.setReadTimeout(8000);
        this.restTemplate = new RestTemplate(factory);
    }

    public List<ProductoSimilarDTO> obtenerSimilares(String idProd) {
        try {
            RecomendacionDTO respuesta = restTemplate.getForObject(
                PYTHON_API_URL + idProd,
                RecomendacionDTO.class
            );
            return (respuesta != null && respuesta.getRecomendaciones() != null)
                ? respuesta.getRecomendaciones()
                : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Servicio Python no disponible: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}