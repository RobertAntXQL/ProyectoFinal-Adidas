package com.adidas.inventario.repository;

import com.adidas.inventario.model.PrediccionDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrediccionDemandaRepository extends JpaRepository<PrediccionDemanda, String> {
    List<PrediccionDemanda> findTop20ByOrderByFechaGeneracionDesc();
}

