package com.portuario.controller;

import com.portuario.model.Puerto;
import com.portuario.repository.PuertoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puertos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PuertoController {

    private final PuertoRepository puertoRepository;

    @GetMapping
    public ResponseEntity<List<Puerto>> listarTodos() {
        return ResponseEntity.ok(puertoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Puerto> buscarPorId(@PathVariable Integer id) {
        return puertoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Puerto> guardar(@RequestBody Puerto puerto) {
        return new ResponseEntity<>(puertoRepository.save(puerto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Puerto> actualizar(@PathVariable Integer id, @RequestBody Puerto puerto) {
        if (!puertoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        puerto.setIdPuerto(id);
        return ResponseEntity.ok(puertoRepository.save(puerto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!puertoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        puertoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
