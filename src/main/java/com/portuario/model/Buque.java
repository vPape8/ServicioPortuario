package com.portuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "buque")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buque {

    @Id
    @Column(name = "cod_buque", length = 50)
    private String codBuque;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "eslora")
    private Double eslora;

    @Column(name = "fecha_llegada")
    private LocalDateTime fechaLlegada;

    @Column(name = "fecha_partida")
    private LocalDateTime fechaPartida;
}
