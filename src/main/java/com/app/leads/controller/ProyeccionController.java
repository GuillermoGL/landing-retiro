package com.app.leads.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import com.app.leads.dto.ProyeccionRequest;
import com.app.leads.entity.Lead;
import com.app.leads.repository.LeadRepository;
import com.app.leads.service.ProyeccionService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProyeccionController {
    
    private final ProyeccionService service;
    private final LeadRepository leadRepo;

    public ProyeccionController(ProyeccionService service, LeadRepository leadRepo) {
        this.service = service;
        this.leadRepo = leadRepo;
    }

    @PostMapping("/proyeccion")
    public ResponseEntity<?> proyeccion(@RequestBody ProyeccionRequest req) {
        try {
            var resp = service.calcular(req);

            // Guarda lead (puedes guardar siempre o solo si no existe)
            Lead lead = new Lead();
            lead.setEmail(req.email().trim().toLowerCase());
            lead.setEdadActual(req.edadActual());
            lead.setEdadRetiro(req.edadRetiro());
            lead.setAporteMensual(req.aporteMensual());
            leadRepo.save(lead);

            // Fase 2: aquí podrías disparar email con Mailchimp/SMTP
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}
