package com.portuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "puerto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Puerto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puerto")
    private Integer idPuerto;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tarifa_hora")
    private Double tarifaHora;

    @Column(name = "tarifa_eslora")
    private Double tarifaEslora;
}
