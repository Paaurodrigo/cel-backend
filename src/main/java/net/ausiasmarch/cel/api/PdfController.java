package net.ausiasmarch.cel.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.entity.InstalacionEntity;
import net.ausiasmarch.cel.repository.InstalacionRepository;
import net.ausiasmarch.cel.service.PdfService;
import net.ausiasmarch.cel.service.TxtService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/generate")
public class PdfController {


@Autowired
private InstalacionRepository instalacionRepository; // ✅ corregido

@Autowired
private PdfService pdfService;

@Autowired
private TxtService txtService;
;


@GetMapping("/pdf/{instalacionId}")
public ResponseEntity<byte[]> generatePdf(@PathVariable Long instalacionId) {
    try {
        byte[] pdfContent = pdfService.generatePdf(instalacionId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=acuerdo-autoconsumo.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    } catch (IOException e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

int anoActual = LocalDate.now().getYear();

@GetMapping("/txt/{id}")
public ResponseEntity<byte[]> generateTxt(@PathVariable("id") Long instalacionId) throws IOException {

    // 🔹 Obtener la instalación
    InstalacionEntity instalacion = instalacionRepository.findById(instalacionId)
        .orElseThrow(() -> new RuntimeException("Instalación no encontrada"));

    // 🔹 Obtener el CAU de la instalación
    String cau = instalacion.getCau();

    // 🔹 Generar el TXT (como antes)
    byte[] txtBytes = txtService.generateTxt(instalacionId);

    // 🔹 Preparar nombre del archivo (con CAU seguro)
    String cauSafe = cau.replaceAll("[^a-zA-Z0-9_-]", "_");
    String nombreArchivo = cauSafe + "_" + anoActual + ".txt";

    // 🔹 Preparar headers de respuesta
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"");

    // 🔹 Devolver respuesta
    return new ResponseEntity<>(txtBytes, headers, HttpStatus.OK);
}
}