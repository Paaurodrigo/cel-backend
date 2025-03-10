package net.ausiasmarch.cel.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import net.ausiasmarch.cel.entity.ConexionEntity;
import net.ausiasmarch.cel.repository.ConexionRepository;
import java.awt.Color;
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

            // 🔹 Agregar Título
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("ACUERDO DE REPARTO DE ENERGÍA DE AUTOCONSUMO COLECTIVO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // 🔹 Información de la Instalación
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("CÓDIGO DE AUTOCONSUMO (CAU): ES0135000361028305LA0FA001", boldFont));
            document.add(new Paragraph("\n"));

            // 🔹 Obtener Datos desde la Base de Datos
            List<ConexionEntity> conexiones = conexionRepository.findByInstalacionId(instalacionId);
            
            if (conexiones.isEmpty()) {
                document.add(new Paragraph("No hay consumidores asociados a esta instalación.", boldFont));
            } else {
                // 🔹 Tabla de Consumidores Asociados
                PdfPTable table = new PdfPTable(5); // ✅ Ahora tiene 5 columnas (incluye Firma)
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setWidths(new float[]{3, 2, 3, 2, 3}); // Ajustar tamaños

                // Encabezados de la tabla
                addTableHeader(table, "Consumidor Asociado");
                addTableHeader(table, "NIF");
                addTableHeader(table, "CUPS");
                addTableHeader(table, "Coeficiente de Reparto (ß)");
                addTableHeader(table, "Firma"); // ✅ Nueva columna

                // Llenar la tabla con datos de la base de datos
                for (ConexionEntity conexion : conexiones) {
                    table.addCell(conexion.getInmueble().getSocio().getNombre()+" "+
                                  conexion.getInmueble().getSocio().getApellido1()+" "+
                                  conexion.getInmueble().getSocio().getApellido2()); // Nombre

                    table.addCell(conexion.getInmueble().getSocio().getDNI()); // NIF

                    table.addCell(conexion.getInmueble().getCups()); // Código CUPS

                    table.addCell(String.valueOf(conexion.getPorcentaje())); // Coeficiente de Reparto

                    // ✅ Agregar Firma
                    if (conexion.getFirma() != null && !conexion.getFirma().isEmpty()) {
                        // 🔹 Agregar Firma
try {
    Image firmaImage = convertirBase64AImagen(conexion.getFirma());
    
    if (firmaImage != null) {
        firmaImage.scaleToFit(70, 40); // Ajustar tamaño de la firma
        PdfPCell firmaCell = new PdfPCell(firmaImage);
        firmaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        firmaCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(firmaCell);
    } else {
        table.addCell("Firma no válida"); // Mostrar un mensaje si la firma no es válida
    }
} catch (Exception e) {
    table.addCell("Error al cargar firma");
    System.err.println("Error al agregar la firma al PDF: " + e.getMessage());
}

                    } else {
                        table.addCell("Sin firma"); // Si no hay firma registrada
                    }
                }

                document.add(table);
                document.add(new Paragraph("\n"));
            }

            // 🔹 Agregar Párrafo Final
            String textoFinal = "Con la firma del presente acuerdo, los consumidores nos acogemos voluntariamente al mecanismo de compensación simplificada...";
            document.add(new Paragraph(textoFinal));

        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF", e);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    // ✅ Método para convertir Base64 a Imagen
    private Image convertirBase64AImagen(String base64) {
        try {
            // Eliminar el prefijo si existe
            if (base64.startsWith("data:image/png;base64,")) {
                base64 = base64.substring("data:image/png;base64,".length());
            }
    
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            return Image.getInstance(imageBytes);
        } catch (BadElementException | IOException e) {
            System.err.println("Error al convertir la firma en imagen: " + e.getMessage());
            return null; // Retorna null si la conversión falla
        }
    }
    
    

    // ✅ Método para agregar encabezados de tabla
    private void addTableHeader(PdfPTable table, String columnTitle) {
        PdfPCell header = new PdfPCell();
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        header.setPhrase(new Phrase(columnTitle, headerFont));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setBackgroundColor(new Color(200, 200, 200));
        table.addCell(header);
    }
}
