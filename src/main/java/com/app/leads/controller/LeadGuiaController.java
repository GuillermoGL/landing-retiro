package com.app.leads.controller;

import com.app.leads.dto.LeadGuiaRequest;
import com.app.leads.dto.LeadGuiaResponse;
import com.app.leads.service.LeadGuiaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadGuiaController {
    private final LeadGuiaService service;

    public LeadGuiaController(LeadGuiaService service) {
        this.service = service;
    }

    @PostMapping("/guia")
    public LeadGuiaResponse guardar(@RequestBody LeadGuiaRequest request) {
        return service.guardar(request);
    }
}
