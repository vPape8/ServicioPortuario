package com.portuario.controller;

import com.portuario.model.Boleta;
import com.portuario.repository.BoletaRepository;
import com.portuario.service.BoletaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaRepository boletaRepository;
    private final BoletaService boletaService;

    @GetMapping
    public ResponseEntity<List<Boleta>> listarTodos() {
        return ResponseEntity.ok(boletaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boleta> buscarPorId(@PathVariable String id) {
        return boletaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Boleta> guardar(@RequestBody Boleta boleta) {
        return new ResponseEntity<>(boletaRepository.save(boleta), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boleta> actualizar(@PathVariable String id, @RequestBody Boleta boleta) {
        if (!boletaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        boleta.setIdBoleta(id);
        return ResponseEntity.ok(boletaRepository.save(boleta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!boletaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        boletaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calcular")
    public ResponseEntity<Boleta> calcular(@RequestBody CalculoRequest request) {
        Boleta boleta = boletaService.generarBoleta(request.getCodBuque(), request.getIdPuerto(), request.getIdFuncionario());
        return new ResponseEntity<>(boleta, HttpStatus.CREATED);
    }

    @Data
    public static class CalculoRequest {
        private String codBuque;
        private Integer idPuerto;
        private Integer idFuncionario;
    }
}
