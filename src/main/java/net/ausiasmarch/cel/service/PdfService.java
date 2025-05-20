package net.ausiasmarch.cel.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.repository.ConexionRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class PdfService {

    private final ConexionRepository conexionRepository;

    public PdfService(ConexionRepository conexionRepository) {
        this.conexionRepository = conexionRepository;
    }

    public byte[] generatePdf(Long instalacionId) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            // 🔹 Título
            Paragraph title = new Paragraph("ACUERDO DE REPARTO DE ENERGÍA DE AUTOCONSUMO COLECTIVO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // 🔹 Información básica
            document.add(new Paragraph("CÓDIGO DE AUTOCONSUMO (CAU): ES0135000361028305LA0FA001", boldFont));
            document.add(new Paragraph("\n"));

            // 🔹 Obtener conexiones
            List<ConexionEntity> conexiones = conexionRepository.findByInstalacionId(instalacionId);

            if (conexiones.isEmpty()) {
                document.add(new Paragraph("No hay consumidores asociados a esta instalación.", boldFont));
            } else {
                // 🔹 Tabla resumen
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setWidths(new float[]{3, 2, 3, 2, 3});
                addTableHeader(table, "Orden");
                addTableHeader(table, "Consumidor Asociado");
                addTableHeader(table, "NIF");
                addTableHeader(table, "CUPS");
                addTableHeader(table, "Coeficiente de Reparto (ß)");
              
                int contador1 = 1;
                for (ConexionEntity conexion : conexiones) {
                    table.addCell(String.valueOf(contador1++));
                    table.addCell(conexion.getInmueble().getSocio().getNombre() + " " +
                            conexion.getInmueble().getSocio().getApellido1() + " " +
                            conexion.getInmueble().getSocio().getApellido2());

                    table.addCell(conexion.getInmueble().getSocio().getDNI());
                    table.addCell(conexion.getInmueble().getCups());
                    table.addCell(String.format("%.3f%%", conexion.getPorcentaje()));

                   
                }

                document.add(table);
                document.add(new Paragraph("\n"));
            }

            // 🔹 Página individual por consumidor
            int contador = 1;
            for (ConexionEntity conexion : conexiones) {
                document.newPage();

                Paragraph consumidorHeader = new Paragraph("Consumidor asociado nº " + contador++, boldFont);
                consumidorHeader.setSpacingAfter(10);
                document.add(consumidorHeader);

                document.add(new Paragraph("Titular: " +
                        conexion.getInmueble().getSocio().getNombre() + " " +
                        conexion.getInmueble().getSocio().getApellido1() + " " +
                        conexion.getInmueble().getSocio().getApellido2()));
                document.add(new Paragraph("NIF:  " + conexion.getInmueble().getSocio().getDNI()));
                document.add(new Paragraph("CUPS: " + conexion.getInmueble().getCups()));
                document.add(new Paragraph("Porcentaje de reparto del autoconsumo colectivo: " +
                        String.format("%.3f%%", conexion.getPorcentaje())));
                document.add(new Paragraph("Firma:\n"));

                if (conexion.getFirma() != null && !conexion.getFirma().isEmpty()) {
                    try {
                        Image firmaImage = convertirBase64AImagen(conexion.getFirma());
                        if (firmaImage != null) {
                            firmaImage.scaleToFit(150, 70);
                            document.add(firmaImage);
                        } else {
                            document.add(new Paragraph("Firma no válida"));
                        }
                    } catch (Exception e) {
                        document.add(new Paragraph("Error al cargar firma"));
                    }
                } else {
                    document.add(new Paragraph("Sin firma"));
                }

                document.add(new Paragraph("\n"));
            }

        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF", e);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String columnTitle) {
        PdfPCell header = new PdfPCell();
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        header.setPhrase(new Phrase(columnTitle, headerFont));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setBackgroundColor(new Color(220, 220, 220));
        table.addCell(header);
    }

    private Image convertirBase64AImagen(String base64) {
        try {
            if (base64.startsWith("data:image/png;base64,")) {
                base64 = base64.substring("data:image/png;base64,".length());
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            return Image.getInstance(imageBytes);
        } catch (BadElementException | IOException e) {
            System.err.println("Error al convertir firma en imagen: " + e.getMessage());
            return null;
        }
    }
}
