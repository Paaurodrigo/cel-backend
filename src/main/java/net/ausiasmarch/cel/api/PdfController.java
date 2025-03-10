package net.ausiasmarch.cel.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.ausiasmarch.cel.service.PdfService;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {


    @Autowired
    private PdfService pdfService;

    @GetMapping("/generate/{instalacionId}")
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

}


