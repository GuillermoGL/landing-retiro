package com.app.leads.dto;

import java.util.List;

public record ProyeccionResponse(
    List<String> labels,
    List<Long> ahorroSimple,
    List<Long> proyeccionEstimada,
    List<Long> valorAjustadoInflacion,
    Long montoFinalEstimado,
    Long ahorroFinal,
    Long ahorroFinalInflacion,
    Integer years,
    Long metaRetiro,
    Double avanceMeta,
    Long faltanteMeta,
    Long ingresoMensualEstimado
) {}
