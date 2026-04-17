package com.app.leads.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.*;

@Data
@Entity
@Table(name = "lead_prospectos")
public class LeadProspecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String whatsapp;

    @Column(nullable = false)
    private String producto;

    @Column(nullable = false)
    private String diagnostico;

    @Column(name = "pension_actual_estimada", precision = 12, scale = 2)
    private BigDecimal pensionActualEstimada;

    @Column(name = "pension_optimizada", precision = 12, scale = 2)
    private BigDecimal pensionOptimizada;

    @Column(name = "incremento_estimado", precision = 12, scale = 2)
    private BigDecimal incrementoEstimado;

    @Column(name = "costo_mensual", precision = 12, scale = 2)
    private BigDecimal costoMensual;

    @Column(name = "costo_total", precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    // getters y setters
}
