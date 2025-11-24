package com.portuario.service;



import com.portuario.model.Boleta;
import com.portuario.model.Buque;
import com.portuario.model.Funcionario;
import com.portuario.model.Puerto;
import com.portuario.repository.BoletaRepository;
import com.portuario.repository.BuqueRepository;
import com.portuario.repository.FuncionarioRepository;
import com.portuario.repository.PuertoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BoletaService {

    @Autowired
    private PuertoRepository puertoRepository;
    @Autowired
    private BuqueRepository buqueRepository;
    @Autowired
    private BoletaRepository boletaRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    // Método simple para calcular costo (sin guardar)
    public double calcular(String codBuque, Integer idPuerto, Integer idFuncionario) {
        Buque buque = buqueRepository.findById(codBuque)
                .orElseThrow(() -> new IllegalArgumentException("Buque no encontrado"));
        Puerto puerto = puertoRepository.findById(idPuerto)
                .orElseThrow(() -> new IllegalArgumentException("Puerto no encontrado"));

        // Calculamos horas dentro del servicio para no depender de clases externas
        long horasEnPuerto = calcularHoras(buque.getFechaLlegada(), buque.getFechaPartida());

        double tarifaBase = puerto.getTarifaHora() > 0
                ? puerto.getTarifaEslora()
                : puerto.getTarifaHora(); // Ajusta esta lógica según tu regla de negocio real

        // Fórmula: (Tarifa * Eslora) + (TarifaHora * Horas) - Ajusta según tu lógica exacta
        return (tarifaBase * buque.getEslora()) + (puerto.getTarifaHora() * horasEnPuerto);
    }

    // Método principal: Calcula, Crea la Boleta y la Guarda en BD
    public Boleta crearYGuardarBoleta(String codBuque, Integer idPuerto, Integer idFuncionario) {
        Buque buque = buqueRepository.findById(codBuque)
                .orElseThrow(() -> new IllegalArgumentException("Buque no encontrado: " + codBuque));
        Puerto puerto = puertoRepository.findById(idPuerto)
                .orElseThrow(() -> new IllegalArgumentException("Puerto no encontrado: " + idPuerto));
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new IllegalArgumentException("Funcionario no encontrado: " + idFuncionario));

        long horasEnPuerto = calcularHoras(buque.getFechaLlegada(), buque.getFechaPartida());

        // Lógica de costo (Asegúrate que coincida con la rúbrica)
        double tarifaBase = (puerto.getTarifaHora() > 0) ? puerto.getTarifaEslora() : puerto.getTarifaHora();
        double costo = (tarifaBase * buque.getEslora()) + (puerto.getTarifaHora() * horasEnPuerto);

        Boleta boleta = new Boleta();
        boleta.setFuncionario(funcionario);
        boleta.setBuque(buque);
        boleta.setPuerto(puerto);
        // Generamos un ID compuesto como string
        boleta.setIdBoleta(codBuque + "-" + idPuerto + "-" + idFuncionario + "-" + System.currentTimeMillis());
        boleta.setMonto(costo);
        boleta.setFechaEmision(new java.sql.Date(System.currentTimeMillis()));

        return boletaRepository.save(boleta);
    }


    public List<Boleta> findAll(){
        return boletaRepository.findAll();
    }

    public Boleta findById(String idBoleta){
        return boletaRepository.findById(idBoleta)
                .orElseThrow(()-> new RuntimeException("Boleta no encontrada"));
    }

    // Método auxiliar privado para calcular horas
    private long calcularHoras(LocalDateTime llegada, LocalDateTime partida) {
        if (llegada == null || partida == null) return 0;
        return Duration.between(llegada, partida).toHours();
    }


    public double calcularSimulacion(double eslora, int dias, String tipoBuque, String servicios, int pasajeros) {
        double tarifaBase = eslora * 10.0;

        switch (tipoBuque.toLowerCase()) {
            case "pesquero": tarifaBase *= 0.8; break;
            case "militar": tarifaBase *= 1.5; break;
            case "investigacion": tarifaBase *= 1.2; break;
            case "crucero": tarifaBase *= 2.0; break;
            default: break;
        }

        double costoPasajeros = pasajeros * 5.0;

        double costoServicios = 0;
        if (servicios != null) {
            switch (servicios.toLowerCase()) {
                case "medio": costoServicios = 500; break;
                case "completo": costoServicios = 1500; break;
                default: break;
            }
        }

        return ((tarifaBase + costoPasajeros) * dias) + costoServicios;
    }
}
