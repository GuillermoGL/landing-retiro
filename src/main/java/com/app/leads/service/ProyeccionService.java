package com.app.leads.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.leads.dto.ProyeccionRequest;
import com.app.leads.dto.ProyeccionResponse;

@Service
public class ProyeccionService {
    public ProyeccionResponse calcular(ProyeccionRequest request) {
        int edadActual = request.edadActual();
        int edadRetiro = request.edadRetiro();
        double aporteMensual = request.aporteMensual().doubleValue();
        long metaRetiro = request.metaRetiro().longValue();
        int aniosDisfruteRetiro = request.aniosDisfruteRetiro() != null ? request.aniosDisfruteRetiro() : 20;

        if (edadRetiro <= edadActual) {
            throw new IllegalArgumentException("La edad de retiro debe ser mayor a la edad actual.");
        }

        int years = edadRetiro - edadActual;

        List<String> labels = new ArrayList<>();
        List<Long> ahorroSimple = new ArrayList<>();
        List<Long> proyeccionEstimada = new ArrayList<>();
        List<Long> valorAjustadoInflacion = new ArrayList<>();

        double ahorroAcumulado = 0;
        double inversionAcumulada = 0;
        double inflacionAnual = 0.04;

        for (int year = 1; year <= years; year++) {
            labels.add("Año " + year);
            ahorroAcumulado += aporteMensual * 12;

            double rendimientoAnual;
            if (year % 5 == 0) {
                rendimientoAnual = 0.03;
            } else if (year % 7 == 0) {
                rendimientoAnual = 0.11;
            } else {
                rendimientoAnual = 0.07;
            }

            for (int m = 0; m < 12; m++) {
                double tasaMensual = rendimientoAnual / 12;
                inversionAcumulada = (inversionAcumulada + aporteMensual) * (1 + tasaMensual);
            }

            double valorReal = inversionAcumulada / Math.pow(1 + inflacionAnual, year);

            ahorroSimple.add(Math.round(ahorroAcumulado));
            proyeccionEstimada.add(Math.round(inversionAcumulada));
            valorAjustadoInflacion.add(Math.round(valorReal));
        }

        long montoFinalEstimado = proyeccionEstimada.get(proyeccionEstimada.size() - 1);
        long ahorroFinal = ahorroSimple.get(ahorroSimple.size() - 1);
        long ahorroFinalInflacion = valorAjustadoInflacion.get(valorAjustadoInflacion.size() - 1);

        double avanceMeta = metaRetiro > 0
                ? Math.min((montoFinalEstimado * 100.0) / metaRetiro, 999.0)
                : 0;

        long faltanteMeta = Math.max(metaRetiro - montoFinalEstimado, 0);

        long ingresoMensualEstimado = aniosDisfruteRetiro > 0
                ? Math.round((double) montoFinalEstimado / (aniosDisfruteRetiro * 12))
                : 0;

        return new ProyeccionResponse(
                labels,
                ahorroSimple,
                proyeccionEstimada,
                valorAjustadoInflacion,
                montoFinalEstimado,
                ahorroFinal,
                ahorroFinalInflacion,
                years,
                metaRetiro,
                Math.round(avanceMeta * 100.0) / 100.0,
                faltanteMeta,
                ingresoMensualEstimado
        );
    }
}
