package com.portuario.controller;

import com.portuario.model.Buque;
import com.portuario.repository.BuqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buques")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BuqueController {

    private final BuqueRepository buqueRepository;

    @GetMapping
    public ResponseEntity<List<Buque>> listarTodos() {
        return ResponseEntity.ok(buqueRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buque> buscarPorId(@PathVariable String id) {
        return buqueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Buque> guardar(@RequestBody Buque buque) {
        return new ResponseEntity<>(buqueRepository.save(buque), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buque> actualizar(@PathVariable String id, @RequestBody Buque buque) {
        if (!buqueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        buque.setCodBuque(id);
        return ResponseEntity.ok(buqueRepository.save(buque));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!buqueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        buqueRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
