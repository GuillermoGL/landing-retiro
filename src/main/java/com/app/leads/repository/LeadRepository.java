package com.app.leads.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.leads.entity.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    boolean existsByEmail(String email);
}
