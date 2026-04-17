package com.app.leads.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ProyeccionRequest(
    @Min(18)
    int edadActual,
    @Min(60)
    int edadRetiro,
    @Positive
    BigDecimal aporteMensual,     // MXN mensual
    @Email
    String email,
    BigDecimal metaRetiro,
    Integer aniosDisfruteRetiro
) {}
