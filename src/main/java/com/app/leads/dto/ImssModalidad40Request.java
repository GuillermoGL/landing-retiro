package com.app.leads.dto;

import java.math.BigDecimal;

public record ImssModalidad40Request(
    Integer edadActual,
    Integer edadRetiro,
    Integer anioInicioCotizacion,
    Integer semanasCotizadas,
    Integer semanasUltimos5Anios,
    BigDecimal salarioPromedio,
    BigDecimal salarioModalidad40,
    Integer aniosModalidad40,
    Integer aniosSinCotizar
) {}
