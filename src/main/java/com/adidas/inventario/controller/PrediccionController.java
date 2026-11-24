package com.adidas.inventario.controller;

import com.adidas.inventario.dto.ForecastResponseDTO;
import com.adidas.inventario.dto.RecomendacionDTO.ProductoSimilarDTO;
import com.adidas.inventario.model.PrediccionDemanda;
import com.adidas.inventario.service.PrediccionDemandaService;
import com.adidas.inventario.service.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PrediccionController {

    @Autowired
    private PrediccionDemandaService prediccionService;

    @Autowired
    private RecomendacionService recomendacionService;

    // 1. Calcular predicción (Random Forest), guardarla en MySQL y devolver JSON
    // POST http://localhost:8080/api/predicciones/generar?horizonte=SEMANAL (o MENSUAL)
    @PostMapping("/predicciones/generar")
    public ResponseEntity<ForecastResponseDTO> generarPrediccion(
            @RequestParam(defaultValue = "SEMANAL") String horizonte) {
        ForecastResponseDTO resultado = prediccionService.ejecutarYGuardarPrediccion(horizonte);
        return ResponseEntity.ok(resultado);
    }

    // 2. Listar el historial de predicciones guardadas en MySQL
    // GET http://localhost:8080/api/predicciones/historial
    @GetMapping("/predicciones/historial")
    public ResponseEntity<List<PrediccionDemanda>> listarHistorial() {
        return ResponseEntity.ok(prediccionService.listarHistorialGuardado());
    }

    // 3. Recomendaciones "También te puede interesar" (Similitud del Coseno)
    // GET http://localhost:8080/api/productos/P001/similares
    @GetMapping("/productos/{id}/similares")
    public ResponseEntity<List<ProductoSimilarDTO>> listarSimilares(@PathVariable("id") String idProd) {
        return ResponseEntity.ok(recomendacionService.obtenerSimilares(idProd));
    }
}
