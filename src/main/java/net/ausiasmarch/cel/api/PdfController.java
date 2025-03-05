package net.ausiasmarch.cel.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.ausiasmarch.cel.service.PdfGenerationService;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @PostMapping("/generatePdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String connectionDetails) {
        try {
            byte[] pdfContent = pdfGenerationService.generateConnectionPdf(connectionDetails);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=connection-details.pdf");
            headers.add("Content-Type", "application/pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}