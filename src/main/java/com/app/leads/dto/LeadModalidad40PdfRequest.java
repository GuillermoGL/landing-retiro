package com.app.leads.dto;

import java.math.BigDecimal;

public record LeadModalidad40PdfRequest(
    String nombre,
    String correo,
    String whatsapp,
    String producto,
    String diagnostico,
    BigDecimal pensionActualEstimada,
    BigDecimal pensionOptimizada,
    BigDecimal incrementoEstimado,
    BigDecimal costoMensual,
    BigDecimal costoTotal
) {}
