package net.ausiasmarch.cel.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.repository.ConexionRepository;

@Service
public class TxtService {

    private final ConexionRepository conexionRepository;

    public TxtService(ConexionRepository conexionRepository) {
        this.conexionRepository = conexionRepository;
    }

    public byte[] generateTxt(Long instalacionId) throws IOException {

        List<ConexionEntity> conexiones = conexionRepository.findByInstalacionId(instalacionId);

        StringBuilder sb = new StringBuilder();

        for (ConexionEntity conexion : conexiones) {
            String cups = conexion.getInmueble().getCups();
            double porcentaje = conexion.getPorcentaje();

            sb.append(cups)
              .append("0F;")
              .append(String.format("%.6f", porcentaje).replace('.', ',')) 
              .append("\n");
        }

        // Devolver como byte[]
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}