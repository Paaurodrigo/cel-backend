package net.ausiasmarch.cel.service;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
@Service
public class PdfGenerationService {
    public byte[] generateConnectionPdf(String connectionDetails) throws IOException {
        System.out.println("Generando PDF con detalles: " + connectionDetails);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Detalles de la conexión:"));
        document.add(new Paragraph(connectionDetails));

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        System.out.println("Tamaño del PDF generado: " + pdfBytes.length + " bytes");

        return pdfBytes;
    }
}