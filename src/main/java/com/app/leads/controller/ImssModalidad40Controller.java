package com.app.leads.controller;

import com.app.leads.dto.ImssModalidad40Request;
import com.app.leads.dto.ImssModalidad40Response;
import com.app.leads.service.ImssModalidad40Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imss")
public class ImssModalidad40Controller {
    private final ImssModalidad40Service service;

    public ImssModalidad40Controller(ImssModalidad40Service service) {
        this.service = service;
    }

    @PostMapping("/modalidad40")
    public ImssModalidad40Response calcular(@RequestBody ImssModalidad40Request request) {
        return service.calcular(request);
    }
}
