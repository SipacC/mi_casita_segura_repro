package Gestion_reserva;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import Modelo.Reserva;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearReservaPDF {

    public static String generarReserva(Reserva reserva, String nombreArea, String nombreUsuario) {
        try {
            // Carpeta base
            String carpeta = "C:/pdfReserva/";
            File dir = new File(carpeta);
            if (!dir.exists()) dir.mkdirs();

            // Nombre del archivo
            String nombreArchivo = "reserva_" + reserva.getId_reserva() + ".pdf";
            String rutaPDF = carpeta + nombreArchivo;

            // Writer + PDFDoc
            PdfWriter writer = new PdfWriter(rutaPDF);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Encabezado
            document.add(new Paragraph("COMPROBANTE DE RESERVA")
                    .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Residencial Mi Casita Segura")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Fecha de emisión: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("\n"));

            // Datos usuario
            document.add(new Paragraph("👤 Residente: " + nombreUsuario));
            document.add(new Paragraph("📌 Área común: " + nombreArea));
            document.add(new Paragraph("📅 Fecha reservada: " + reserva.getFecha_reserva()));
            document.add(new Paragraph("🕒 Horario: " + reserva.getHora_inicio() + " - " + reserva.getHora_fin()));
            document.add(new Paragraph("📄 Estado: " + reserva.getEstado()));
            document.add(new Paragraph("\n"));

            // Tabla básica
            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 4}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell(new Cell().add(new Paragraph("Número de Reserva").setBold()));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(reserva.getId_reserva()))));
            table.addCell(new Cell().add(new Paragraph("Área Común").setBold()));
            table.addCell(new Cell().add(new Paragraph(nombreArea)));
            table.addCell(new Cell().add(new Paragraph("Fecha").setBold()));
            table.addCell(new Cell().add(new Paragraph(reserva.getFecha_reserva())));
            table.addCell(new Cell().add(new Paragraph("Hora inicio").setBold()));
            table.addCell(new Cell().add(new Paragraph(reserva.getHora_inicio())));
            table.addCell(new Cell().add(new Paragraph("Hora fin").setBold()));
            table.addCell(new Cell().add(new Paragraph(reserva.getHora_fin())));
            document.add(table);

            // Mensaje final
            document.add(new Paragraph("\nGracias por usar el sistema de reservas. 🎉")
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            System.out.println("✅ Reserva generada en: " + rutaPDF);
            return rutaPDF; // 🔹 nunca devolvemos null si se genera bien

        } catch (Exception e) {
            System.err.println("❌ Error al generar reserva: " + e.getMessage());
            e.printStackTrace();
            return null; // si falla, retorna null
        }
    }
}
