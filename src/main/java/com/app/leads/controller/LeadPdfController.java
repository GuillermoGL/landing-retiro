package com.app.leads.controller;

import com.app.leads.dto.LeadModalidad40PdfRequest;
import com.app.leads.service.LeadPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadPdfController {
    private final LeadPdfService service;

    public LeadPdfController(LeadPdfService service) {
        this.service = service;
    }

    @PostMapping("/modalidad40/pdf")
    public ResponseEntity<byte[]> generarPdf(@RequestBody LeadModalidad40PdfRequest request) {
        byte[] pdf = service.guardarYGenerarPdf(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resultado-modalidad-40.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
