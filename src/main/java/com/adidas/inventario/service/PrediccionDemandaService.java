package com.adidas.inventario.service;

import com.adidas.inventario.dto.ForecastResponseDTO;
import com.adidas.inventario.dto.ForecastResponseDTO.ItemPrediccionDTO;
import com.adidas.inventario.model.PrediccionDemanda;
import com.adidas.inventario.repository.PrediccionDemandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PrediccionDemandaService {

    @Autowired
    private PrediccionDemandaRepository prediccionRepository;

    private final RestTemplate restTemplate;
    private static final String PYTHON_FORECAST_URL = "http://localhost:8000/predecir?horizonte=";

    public PrediccionDemandaService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(15000);
        this.restTemplate = new RestTemplate(factory);
    }

    public ForecastResponseDTO ejecutarYGuardarPrediccion(String horizonte) {
        ForecastResponseDTO respuesta = restTemplate.getForObject(
            PYTHON_FORECAST_URL + horizonte,
            ForecastResponseDTO.class
        );

        if (respuesta != null && respuesta.getPredicciones() != null) {
            LocalDateTime ahora = LocalDateTime.now();
            for (ItemPrediccionDTO item : respuesta.getPredicciones()) {
                PrediccionDemanda entidad = new PrediccionDemanda();
                entidad.setIdPrediccion(UUID.randomUUID().toString());
                entidad.setIdProd(item.getIdProd());
                entidad.setHorizonte(respuesta.getHorizonte());
                entidad.setPeriodoObjetivo(respuesta.getPeriodoObjetivo());
                entidad.setCantidadEstimada(item.getCantidadEstimada());
                entidad.setStockActual(item.getStockActual());
                entidad.setDeficitEstimado(item.getDeficitEstimado());
                entidad.setMaeModelo(respuesta.getMetricasEvaluacion().getMaePares());
                entidad.setFechaGeneracion(ahora);
                prediccionRepository.save(entidad);
            }
        }
        return respuesta;
    }

    public List<PrediccionDemanda> listarHistorialGuardado() {
        return prediccionRepository.findTop20ByOrderByFechaGeneracionDesc();
    }
}

