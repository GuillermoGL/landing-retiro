package com.app.leads.dto;

import com.app.leads.model.DiagnosticoModalidad40;

public record ImssModalidad40Response(
    DiagnosticoModalidad40 diagnostico,
    String mensajeDiagnostico,
    Integer semanasActuales,
    Integer semanasFinales,
    Double costoMensual,
    Double costoTotal,
    Double pensionActualEstimada,
    Double pensionOptimizada,
    Double incrementoEstimado
) {

}
