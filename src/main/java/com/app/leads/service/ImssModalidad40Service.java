package com.app.leads.service;

import com.app.leads.dto.ImssModalidad40Request;
import com.app.leads.dto.ImssModalidad40Response;
import com.app.leads.model.DiagnosticoModalidad40;
import org.springframework.stereotype.Service;

@Service
public class ImssModalidad40Service {

    // Déjala configurable después en application.yml
    private static final double TASA_MODALIDAD40 = 0.11166;

    public ImssModalidad40Response calcular(ImssModalidad40Request request) {

        validarRequest(request);

        int edadRetiro = request.edadRetiro();
        int semanasCotizadas = request.semanasCotizadas();
        int semanasUltimos5Anios = request.semanasUltimos5Anios();
        int aniosModalidad40 = request.aniosModalidad40();
        int aniosSinCotizar = request.aniosSinCotizar();
        int anioInicio = request.anioInicioCotizacion();

        double salarioPromedio = request.salarioPromedio().doubleValue();
        double salarioModalidad40 = request.salarioModalidad40().doubleValue();

        boolean regimen73 = anioInicio < 1997;
        boolean cumple500Semanas = semanasCotizadas >= 500;
        boolean bajaValida = aniosSinCotizar <= 5;
        boolean semanasRecientes = semanasUltimos5Anios >= 52;
        boolean edadValida = edadRetiro >= 60;

        DiagnosticoModalidad40 diagnostico;
        String mensajeDiagnostico;

        if (!edadValida) {
            diagnostico = DiagnosticoModalidad40.NO_ELEGIBLE;
            mensajeDiagnostico = "La pensión por cesantía/vejez requiere al menos 60 años.";
        } else if (!regimen73) {
            diagnostico = DiagnosticoModalidad40.REQUIERE_VALIDACION;
            mensajeDiagnostico = "Tu año de inicio de cotización no corresponde claramente a Ley 73; conviene revisar el caso antes de proyectar Modalidad 40.";
        } else if (!cumple500Semanas) {
            diagnostico = DiagnosticoModalidad40.NO_ELEGIBLE;
            mensajeDiagnostico = "Aún no cumples con las 500 semanas mínimas para Ley 73.";
        } else if (!bajaValida || !semanasRecientes) {
            diagnostico = DiagnosticoModalidad40.REQUIERE_VALIDACION;
            mensajeDiagnostico = "Tu caso necesita validar conservación de derechos o semanas recientes para continuación voluntaria.";
        } else {
            diagnostico = DiagnosticoModalidad40.ELEGIBLE;
            mensajeDiagnostico = "Perfil apto para revisar una estrategia de Modalidad 40.";
        }

        int semanasAdicionales = aniosModalidad40 * 52;
        int semanasFinales = semanasCotizadas + semanasAdicionales;

        double costoMensual = salarioModalidad40 * TASA_MODALIDAD40;
        double costoTotal = costoMensual * 12 * aniosModalidad40;

        double factorEdad = obtenerFactorEdad(edadRetiro);
        double factorSemanasActual = obtenerFactorSemanas(semanasCotizadas);
        double factorSemanasFinal = obtenerFactorSemanas(semanasFinales);

        double pensionActual = salarioPromedio * factorEdad * factorSemanasActual;
        double pensionOptimizada = salarioModalidad40 * factorEdad * factorSemanasFinal;

        // Evita negativos raros
        double incremento = Math.max(pensionOptimizada - pensionActual, 0);

        return new ImssModalidad40Response(
                diagnostico,
                mensajeDiagnostico,
                semanasCotizadas,
                semanasFinales,
                redondear(costoMensual),
                redondear(costoTotal),
                redondear(pensionActual),
                redondear(pensionOptimizada),
                redondear(incremento)
        );
    }

    private void validarRequest(ImssModalidad40Request request) {
        if (request.edadActual() == null || request.edadRetiro() == null ||
            request.anioInicioCotizacion() == null || request.semanasCotizadas() == null ||
            request.semanasUltimos5Anios() == null || request.salarioPromedio() == null ||
            request.salarioModalidad40() == null || request.aniosModalidad40() == null ||
            request.aniosSinCotizar() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        if (request.edadRetiro() < request.edadActual()) {
            throw new IllegalArgumentException("La edad de retiro no puede ser menor a la edad actual.");
        }

        if (request.salarioPromedio().doubleValue() <= 0 || request.salarioModalidad40().doubleValue() <= 0) {
            throw new IllegalArgumentException("Los salarios deben ser mayores a cero.");
        }

        if (request.semanasCotizadas() < 0 || request.semanasUltimos5Anios() < 0 || request.aniosModalidad40() < 0 || request.aniosSinCotizar() < 0) {
            throw new IllegalArgumentException("Semanas y años no pueden ser negativos.");
        }
    }

    private double obtenerFactorEdad(int edadRetiro) {
        if (edadRetiro >= 65) return 1.00;
        if (edadRetiro == 64) return 0.95;
        if (edadRetiro == 63) return 0.90;
        if (edadRetiro == 62) return 0.85;
        if (edadRetiro == 61) return 0.80;
        if (edadRetiro == 60) return 0.75;
        return 0.0;
    }

    private double obtenerFactorSemanas(int semanas) {
        if (semanas < 500) return 0.0;

        // Versión orientativa:
        // 500 semanas = base menor
        // 1000 semanas = referencia sólida
        // tope suave para no disparar demasiado
        double factor = semanas / 1000.0;
        return Math.min(Math.max(factor, 0.55), 1.25);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
