package com.app.leads.dto;

public record LeadGuiaRequest(
    String nombre,
    String correo,
    String whatsapp,
    String interes,
    String origen
) {}
