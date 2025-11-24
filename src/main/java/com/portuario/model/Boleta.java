package com.portuario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boleta {

    @Id
    @Column(name = "id_boleta", length = 50)
    private String idBoleta;

    @Column(name = "monto")
    private Double monto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_emision")
    private Date fechaEmision;

    // CAMBIO IMPORTANTE: De FetchType.LAZY a FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cod_buque", nullable = false)
    private Buque buque;

    // CAMBIO IMPORTANTE: De FetchType.LAZY a FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_puerto", nullable = false)
    private Puerto puerto;

    // CAMBIO IMPORTANTE: De FetchType.LAZY a FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_funcionario", nullable = false)
    private Funcionario funcionario;
}