package com.adidas.inventario.controller;

import com.adidas.inventario.dto.ForecastResponseDTO;
import com.adidas.inventario.dto.RecomendacionDTO.ProductoSimilarDTO;
import com.adidas.inventario.service.PrediccionDemandaService;
import com.adidas.inventario.service.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PrediccionController {

    @Autowired
    private PrediccionDemandaService prediccionService;

    @Autowired
    private RecomendacionService recomendacionService;

    // 1. Pantalla extra de Predicción de Demanda
    @GetMapping("/predicciones")
    public String verPantallaPredicciones(Model model) {
        model.addAttribute("historial", prediccionService.listarHistorialGuardado());
        return "predicciones";
    }

    @PostMapping("/predicciones/generar")
    public String generarPrediccion(@RequestParam(defaultValue = "SEMANAL") String horizonte, Model model) {
        try {
            ForecastResponseDTO resultado = prediccionService.ejecutarYGuardarPrediccion(horizonte);
            model.addAttribute("resultado", resultado);
        } catch (Exception e) {
            model.addAttribute("error", "Error al conectar con Python (puerto 8000): " + e.getMessage());
        }
        model.addAttribute("historial", prediccionService.listarHistorialGuardado());
        return "predicciones";
    }

    // 2. Endpoint REST para "También te puede interesar" (Similitud del Coseno)
    @GetMapping("/api/productos/{id}/similares")
    @ResponseBody
    public ResponseEntity<List<ProductoSimilarDTO>> listarSimilares(@PathVariable("id") String idProd) {
        return ResponseEntity.ok(recomendacionService.obtenerSimilares(idProd));
    }
}

