package com.portuario.controller;

import com.portuario.model.Boleta;
import com.portuario.repository.BoletaRepository;
import com.portuario.service.BoletaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaRepository boletaRepository;
    private final BoletaService boletaService;

    @GetMapping
    public ResponseEntity<List<Boleta>> getAll() {
        return ResponseEntity.ok(boletaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boleta> getById(@PathVariable String id) {
        return boletaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Boleta> create(@RequestBody Boleta boleta) {
        Boleta saved = boletaRepository.save(boleta);
        return ResponseEntity.created(URI.create("/api/boletas/" + saved.getIdBoleta())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boleta> update(@PathVariable String id, @RequestBody Boleta boleta) {
        if (!boletaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        boleta.setIdBoleta(id);
        Boleta updated = boletaRepository.save(boleta);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!boletaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        boletaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calcular")
    public ResponseEntity<Boleta> calcular(@RequestBody CalculoRequest request) {
        Boleta boleta = boletaService.crearYGuardarBoleta(
                request.getCodBuque(),
                request.getIdPuerto(),
                request.getIdFuncionario()
        );
        return ResponseEntity.created(URI.create("/api/boletas/" + boleta.getIdBoleta())).body(boleta);
    }

    // ... código existente ...

    // NUEVO: Endpoint para recibir datos manuales
    @PostMapping("/simular")
    public ResponseEntity<Double> simular(@RequestBody SimulacionRequest request) {
        // Manejo de nulos para seguridad
        int pas = request.getPasajeros() != null ? request.getPasajeros() : 0;
        String serv = request.getServicios() != null ? request.getServicios() : "basico";

        double costo = boletaService.calcularSimulacion(
                request.getEslora(),
                request.getDias(),
                request.getTipoBuque(),
                serv,
                pas
        );
        return ResponseEntity.ok(costo);
    }

    // DTO interno para recibir el JSON manual
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimulacionRequest {
        private Double eslora;
        private Integer dias;
        private String tipoBuque;
        private String servicios;
        private Integer pasajeros;
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculoRequest {
        private String codBuque;
        private Integer idPuerto;
        private Integer idFuncionario;
    }
}