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
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;




@Service
public class PdfService {
    LocalDate fechaHoy = LocalDate.now();
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy")
                                                .withLocale(new Locale("es", "ES"));

    String fechaFormateada = fechaHoy.format(formatter);
    
    
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

            // 🔹 Obtener conexiones
            List<ConexionEntity> conexiones = conexionRepository.findByInstalacionId(instalacionId);

            if (conexiones.isEmpty()) {
                document.add(new Paragraph("No hay consumidores asociados a esta instalación.", boldFont));
            } else {
                String cau = null;

                if (!conexiones.isEmpty()) {
                    cau = conexiones.get(0).getInstalacion().getCau();
                } else {
                    cau = "CAU NO DISPONIBLE";
                }
                
                // 🔹 Generar Acuerdo de Reparto de Energía

Paragraph titulo = new Paragraph("ACUERDO DE REPARTO DE ENERGÍA DE AUTOCONSUMO COLECTIVO", boldFont);
titulo.setAlignment(Element.ALIGN_CENTER);
document.add(titulo);

Paragraph subtitulo1 = new Paragraph("INSTALACIONES CON EXCEDENTES", boldFont);
subtitulo1.setAlignment(Element.ALIGN_CENTER);
document.add(subtitulo1);

Paragraph subtitulo2 = new Paragraph("ACOGIDAS A COMPENSACIÓN", boldFont);
subtitulo2.setAlignment(Element.ALIGN_CENTER);
document.add(subtitulo2);

document.add(new Paragraph("\n"));

Paragraph textoIntro = new Paragraph(
    "En aplicación del Real Decreto 244/2019 de 5 de abril, los siguientes consumidores acordamos " +
    "asociarnos a la instalación de *autoconsumo colectivo de energía eléctrica* con las siguientes características:"
    
);
textoIntro.setAlignment(Element.ALIGN_JUSTIFIED);
document.add(textoIntro);

document.add(new Paragraph("\n"));

// 🔹 Cuadro de selección "CON excedentes / Acogida a compensación"

PdfPTable cuadro = new PdfPTable(2);
cuadro.setWidthPercentage(50);
cuadro.setSpacingBefore(10);
cuadro.setHorizontalAlignment(Element.ALIGN_LEFT);
cuadro.setWidths(new float[]{1, 5});

// Celda con la X
PdfPCell celdaX = new PdfPCell(new Phrase("X", boldFont));
celdaX.setHorizontalAlignment(Element.ALIGN_CENTER);
celdaX.setVerticalAlignment(Element.ALIGN_MIDDLE);
celdaX.setFixedHeight(20);
cuadro.addCell(celdaX);

// Celda con el texto
PdfPCell celdaTexto = new PdfPCell();
celdaTexto.setPaddingLeft(10);
celdaTexto.addElement(new Paragraph("CON excedentes", boldFont));
celdaTexto.addElement(new Paragraph("Acogida a compensación"));
cuadro.addCell(celdaTexto);

document.add(cuadro);

document.add(new Paragraph("\n"));

// 🔹 Código de Autoconsumo (CAU)

PdfPTable cauTable = new PdfPTable(2);
cauTable.setWidthPercentage(100);
cauTable.setSpacingBefore(20);
cauTable.setWidths(new float[]{1, 1});

PdfPCell cauLabel = new PdfPCell(new Phrase("CÓDIGO DE AUTOCONSUMO (CAU)", boldFont));
cauLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
cauLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
cauLabel.setFixedHeight(30);
cauTable.addCell(cauLabel);



PdfPCell cauValue = new PdfPCell(new Phrase(cau)); // NO te olvides de cerrar el paréntesis!
cauValue.setHorizontalAlignment(Element.ALIGN_CENTER);
cauValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
cauValue.setFixedHeight(30);
cauTable.addCell(cauValue);

document.add(cauTable);

document.add(new Paragraph("\n"));
                // 🔹 Tabla resumen
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setWidths(new float[]{1, 5, 2, 4, 2}); // 5 columnas


                // Columna "Consumidor asociado" que ocupa 2 columnas
                PdfPCell cell = new PdfPCell();
                cell.setColspan(2);
                cell.setBackgroundColor(new Color(220, 220, 220)); // Color de java.awt.Color
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.addElement(new Paragraph("CONSUMIDOR ASOCIADO", boldFont));
                cell.addElement(new Paragraph("(titular del suministro)"));
                table.addCell(cell);

                PdfPCell cellNIF = new PdfPCell();
            
                cellNIF.setBackgroundColor(new Color(220, 220, 220)); // Color de java.awt.Color
                cellNIF.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNIF.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellNIF.addElement(new Paragraph("NIF", boldFont));
                table.addCell(cellNIF);
      
                PdfPCell cellCUPS = new PdfPCell();
           
                cellCUPS.setBackgroundColor(new Color(220, 220, 220)); // Color de java.awt.Color
                cellCUPS.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellCUPS.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellCUPS.addElement(new Paragraph("CUPS", boldFont));
                table.addCell(cellCUPS);
    
                // Columna "Coeficiente de Reparto (ß)"
                PdfPCell coefCell = new PdfPCell();
                
                coefCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                coefCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                coefCell.setBackgroundColor(new Color(220, 220, 220)); // Color de java.awt.Color
                coefCell.addElement(new Paragraph("COEF. DE REPARTO (ß)", boldFont));
                table.addCell(coefCell);
                
                int contador1 = 1;
                for (ConexionEntity conexion : conexiones) {
                    // Orden
                    PdfPCell cellOrden = new PdfPCell(new Phrase(String.valueOf(contador1++)));
                    cellOrden.setPaddingTop(12);
                    cellOrden.setPaddingBottom(12);
                    table.addCell(cellOrden);
                
                    // Nombre
                    PdfPCell cellNombre = new PdfPCell(new Phrase(conexion.getInmueble().getSocio().getNombre() + " " +
                                                                  conexion.getInmueble().getSocio().getApellido1() + " " +
                                                                  conexion.getInmueble().getSocio().getApellido2()));
                    cellNombre.setPaddingTop(12);
                    cellNombre.setPaddingBottom(12);
                    table.addCell(cellNombre);
                
                    // DNI
                    PdfPCell cellDni = new PdfPCell(new Phrase(conexion.getInmueble().getSocio().getDNI()));
                    cellDni.setPaddingTop(12);
                    cellDni.setPaddingBottom(12);
                    table.addCell(cellDni);
                
                    // CUPS
                    PdfPCell cellCups = new PdfPCell(new Phrase(conexion.getInmueble().getCups()));
                    cellCups.setPaddingTop(12);
                    cellCups.setPaddingBottom(12);
                    table.addCell(cellCups);
                
                    // Porcentaje
                    PdfPCell cellPorcentaje = new PdfPCell(new Phrase(String.format("%.5f", conexion.getPorcentaje())));
                    cellPorcentaje.setPaddingTop(12);
                    cellPorcentaje.setPaddingBottom(12);
                    table.addCell(cellPorcentaje);
                }
                

                document.add(table);
                
            }

            
           
            document.newPage();
            // Añadir texto introductorio
            Paragraph introProductores = new Paragraph(
                    "(Si existen varios productores con instalaciones de generación asociadas al autoconsumo, completar para cada uno de ellos)"
                    );
            introProductores.setAlignment(Element.ALIGN_LEFT);
            introProductores.setSpacingAfter(10);
            document.add(introProductores);

            // Crear la tabla (5 columnas)
            PdfPTable productoresTable = new PdfPTable(4);
            productoresTable.setWidthPercentage(100);
            productoresTable.setSpacingBefore(10);
            productoresTable.setWidths(new float[] { 1, 5, 2, 4, 2 });

            // Encabezados
            
            PdfPCell cellProd = new PdfPCell();
            cellProd.setColspan(2);
            cellProd.setBackgroundColor(new Color(220, 220, 220));
            cellProd.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellProd.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellProd.addElement(new Paragraph("PRODUCTOR ASOCIADO", boldFont));
            cellProd.addElement(new Paragraph("(titular de la instalación de generación)"));
            productoresTable.addCell(cellProd);

            addTableHeader(productoresTable, "NIF");
            addTableHeader(productoresTable, "CIL");
            PdfPCell cellCoef = new PdfPCell();
            cellCoef.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCoef.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellCoef.setBackgroundColor(new Color(220, 220, 220));
            cellCoef.addElement(new Paragraph("COEFICIENTE", boldFont));
            cellCoef.addElement(new Paragraph("(α)", boldFont));
            productoresTable.addCell(cellCoef);

         
            // Fila vacía (fila 2 vacía como en tu imagen)
            PdfPCell cellOrden2 = new PdfPCell(new Phrase("1"));
            cellOrden2.setFixedHeight(120); // 4 cm
            productoresTable.addCell(cellOrden2);

            PdfPCell cellProd2 = new PdfPCell(new Phrase(""));
            cellProd2.setFixedHeight(120); // 4 cm
            productoresTable.addCell(cellProd2);

            PdfPCell cellNif2 = new PdfPCell(new Phrase(""));
            cellNif2.setFixedHeight(120); // 4 cm
            productoresTable.addCell(cellNif2);

            PdfPCell cellCil2 = new PdfPCell(new Phrase(""));
            cellCil2.setFixedHeight(120); // 4 cm
            productoresTable.addCell(cellCil2);

            PdfPCell cellCoef2 = new PdfPCell(new Phrase(""));
            cellCoef2.setFixedHeight(120); // 4 cm
            productoresTable.addCell(cellCoef2);

            // Añadir la tabla al documento
            document.add(productoresTable);

            document.add(new Paragraph("\n"));
            

            Paragraph parrafoMitad = new Paragraph();
            parrafoMitad.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafoMitad.setSpacingBefore(20); // espacio antes
            parrafoMitad.setSpacingAfter(10); // espacio después
            
            parrafoMitad.add(new Phrase(
                "Con la firma del presente acuerdo, los consumidores nos acogemos voluntariamente al "
              + "mecanismo de compensación simplificada entre los déficits del consumo de cada consumidor "
              + "y la totalidad de los excedentes de la instalación de autoconsumo, tal como establece el Real "
              + "Decreto 244/2019, de 5 de abril.\n\n"
              + "Les rogamos reciban esta comunicación y procedan a realizar los trámites necesarios.\n\n"
              + "Del mismo modo, les solicitamos la aplicación del mecanismo de compensación simplificada de "
              + "los excedentes de la instalación de autoconsumo a la que nos asociamos, y el inicio del "
              + "mecanismo de compensación en el siguiente periodo de facturación desde la recepción de este "
              + "acuerdo.\n\n"
              + "En Valencia, a " + fechaFormateada + ".\n\n"
              + "Los CONSUMIDORES asociados:"
            ));
            
            document.add(parrafoMitad);

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
                String.format("%.3f%%", conexion.getPorcentaje() * 100)));
            
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
            document.newPage();

            
            Paragraph productorheader = new Paragraph("Los PRODUCTORES asociados:", boldFont);
            productorheader.setSpacingAfter(10);
            document.add(productorheader);
            document.add(new Paragraph("PRODUCTOR 1:" + "\n \n \n \n \n "  ));
            document.add(new Paragraph("NIF:" + "\n"));



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
