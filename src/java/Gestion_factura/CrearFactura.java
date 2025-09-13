package Gestion_factura;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import Modelo.Pago;
import Modelo.Persona;
import Modelo.TipoPago;
import ModeloDAO.TipoPagoDAO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearFactura {

    public static String generarFactura(Pago pago, Persona usuario, String numeroFactura) {
    try {
        String carpeta = "C:/facturasProyecto/";
        File dir = new File(carpeta);
        if (!dir.exists()) dir.mkdirs();
        
        String nombreArchivo = "factura_" + numeroFactura + ".pdf";
        String rutaPDF = carpeta + nombreArchivo;

        PdfWriter writer = new PdfWriter(rutaPDF);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("FACTURA ELECTRÓNICA")
                .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Residencial Mi Casita Segura")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Datos del Residente").setBold());
        document.add(new Paragraph("Nombre: " + usuario.getNombres()));
        document.add(new Paragraph("Correo: " + usuario.getCorreo()));
        document.add(new Paragraph("Lote/Casa: " + usuario.getLote() + " - " + usuario.getNumero_casa()));
        document.add(new Paragraph("\n"));

        TipoPagoDAO tipoDAO = new TipoPagoDAO();
        TipoPago tipo = tipoDAO.buscarPorId(pago.getId_tipo());
        String nombreTipo = (tipo != null) ? tipo.getNombre() : "Desconocido";

        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addCell(new Cell().add(new Paragraph("Tipo de Pago").setBold()));
        table.addCell(new Cell().add(new Paragraph("Monto").setBold()));
        table.addCell(new Cell().add(new Paragraph("Mora").setBold()));
        table.addCell(new Cell().add(new Paragraph("Estado").setBold()));

        table.addCell(nombreTipo);
        table.addCell("Q. " + pago.getMonto());
        table.addCell("Q. " + pago.getMora());
        table.addCell(pago.getEstado());

        document.add(table);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Se ha realizado el pago de " + nombreTipo + " con éxito.")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold());

        document.close();
        System.out.println("Factura generada en: " + rutaPDF);

        return rutaPDF;
    } catch (Exception e) {
        System.err.println("Error al generar factura: " + e.getMessage());
        return null;
    }
}

}
