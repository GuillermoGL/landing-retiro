package com.app.leads.service;


import com.app.leads.config.MailchimpProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class MailchimpService {
    private final MailchimpProperties properties;
    private final RestClient restClient;

    public MailchimpService(MailchimpProperties properties) {
        this.properties = properties;

        this.restClient = RestClient.builder()
                .baseUrl("https://" + properties.getServerPrefix() + ".api.mailchimp.com/3.0")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, buildAuth(properties.getApiKey()))
                .build();
    }

    public void enviarLead(String nombre, String correo, String interes, String origen) {

        String email = correo.toLowerCase().trim();
        String hash = md5(email);

        // ---------------------------
        // 1️⃣ Crear o actualizar contacto
        // ---------------------------
        Map<String, Object> body = Map.of(
                "email_address", email,
                "status_if_new", "subscribed",
                "status", "subscribed",
                "merge_fields", Map.of(
                        "FNAME", nombre != null ? nombre : ""
                )
        );

        restClient.put()
                .uri("/lists/{listId}/members/{subscriberHash}",
                        properties.getAudienceId(), hash)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        // ---------------------------
        // 2️⃣ Agregar tags
        // ---------------------------
        List<Map<String, String>> tags = List.of(
                Map.of("name", interes, "status", "active"),
                Map.of("name", origen, "status", "active")
        );

        restClient.post()
                .uri("/lists/{listId}/members/{subscriberHash}/tags",
                        properties.getAudienceId(), hash)
                .body(Map.of("tags", tags))
                .retrieve()
                .toBodilessEntity();
    }

    // ---------------------------
    // AUTH
    // ---------------------------
    private String buildAuth(String apiKey) {
        String raw = "anystring:" + apiKey;
        return "Basic " + Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    // ---------------------------
    // MD5 (Mailchimp lo exige)
    // ---------------------------
    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash MD5", e);
        }
    }
}
