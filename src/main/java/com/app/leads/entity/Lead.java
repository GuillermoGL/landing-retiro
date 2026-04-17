package com.app.leads.entity;

import java.math.BigDecimal;
import java.time.Instant;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Table(name="leads")
public class Lead {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String email;
    @Column(name = "edad_actual")
    private Integer edadActual;
    @Column(name = "edad_retiro")
    private Integer edadRetiro;
    @Column(name = "aporte_mensual")
    private BigDecimal aporteMensual;

    @Column(nullable=false)
    private Instant createdAt = Instant.now();

}
