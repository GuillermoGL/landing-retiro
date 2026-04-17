package com.app.leads.service;

import com.app.leads.entity.LeadGuia;
import com.app.leads.dto.LeadGuiaRequest;
import com.app.leads.dto.LeadGuiaResponse;
import com.app.leads.repository.LeadGuiaRepository;
import org.springframework.stereotype.Service;

@Service
public class LeadGuiaService {

    private final LeadGuiaRepository repository;
    private final MailchimpService mailchimpService;

    public LeadGuiaService(LeadGuiaRepository repository, MailchimpService mailchimpService) {
        this.repository = repository;
        this.mailchimpService = mailchimpService;
    }

    public LeadGuiaResponse guardar(LeadGuiaRequest request) {
        LeadGuia lead = new LeadGuia();
        String interes = request.interes();
        String origen = "";

        System.out.println("interes ant: "+ interes);

        if ("MODALIDAD_40".equals(interes)) {
            origen = "SIMULADOR_MODALIDAD_40";
        } else if ("PENSION_IMSS".equals(interes)) {
            origen = "SIMULADOR_PENSION_IMSS";
        } else if ("PENSION_ISSSTE".equals(interes)) {
            origen = "SIMULADOR_PENSION_ISSSTE";
        } else if ("SUBCUENTA_VIVIENDA".equals(interes)) {
            origen = "SIMULADOR_SUBCUENTA_VIVIENDA";
        } else {
            origen = request.origen();
        }
        System.out.println("origen: "+origen);
        lead.setNombre(request.nombre());
        lead.setCorreo(request.correo());
        lead.setWhatsapp(request.whatsapp());
        lead.setInteres(request.interes());
        lead.setOrigen(origen);

        LeadGuia saved = repository.save(lead);
        try {
            mailchimpService.enviarLead(
            request.nombre(),
            request.correo(),
            request.interes(),
            origen
        );
        } catch (Exception e) {
            System.out.println("Error enviando a Mailchimp: " + e.getMessage());
        }

        return new LeadGuiaResponse(
                "Lead registrado correctamente",
                saved.getId()
        );
    }
}
