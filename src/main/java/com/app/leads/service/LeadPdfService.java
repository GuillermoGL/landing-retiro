package com.app.leads.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.app.leads.dto.LeadModalidad40PdfRequest;
import com.app.leads.entity.LeadProspecto;
import com.app.leads.repository.LeadProspectoRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class LeadPdfService {
    private final LeadProspectoRepository repository;

    public LeadPdfService(LeadProspectoRepository repository) {
        this.repository = repository;
    }

    public byte[] guardarYGenerarPdf(LeadModalidad40PdfRequest request) {
        LeadProspecto lead = new LeadProspecto();
        lead.setNombre(request.nombre());
        lead.setCorreo(request.correo());
        lead.setWhatsapp(request.whatsapp());
        lead.setProducto(request.producto());
        lead.setDiagnostico(request.diagnostico());
        lead.setPensionActualEstimada(request.pensionActualEstimada());
        lead.setPensionOptimizada(request.pensionOptimizada());
        lead.setIncrementoEstimado(request.incrementoEstimado());
        lead.setCostoMensual(request.costoMensual());
        lead.setCostoTotal(request.costoTotal());

        repository.save(lead);

        return generarPdf(request);
    }

    private byte[] generarPdf(LeadModalidad40PdfRequest request) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL);

            document.add(new Paragraph("Resultado estimado - Modalidad 40 IMSS", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Nombre: " + request.nombre(), normalFont));
            document.add(new Paragraph("Correo: " + request.correo(), normalFont));
            document.add(new Paragraph("WhatsApp: " + request.whatsapp(), normalFont));
            document.add(new Paragraph("Diagnóstico: " + request.diagnostico(), normalFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Pensión actual estimada: $" + request.pensionActualEstimada(), normalFont));
            document.add(new Paragraph("Pensión optimizada: $" + request.pensionOptimizada(), normalFont));
            document.add(new Paragraph("Incremento estimado: $" + request.incrementoEstimado(), normalFont));
            document.add(new Paragraph("Costo mensual estimado: $" + request.costoMensual(), normalFont));
            document.add(new Paragraph("Costo total estimado: $" + request.costoTotal(), normalFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                    "Importante: estas no son cifras oficiales. Son estimaciones aproximadas con fines informativos, calculadas con base en criterios generales y reglas públicas del IMSS. El resultado final puede variar según semanas reconocidas, salario pensionable, conservación de derechos y validación documental.",
                    normalFont
            ));

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }
}
