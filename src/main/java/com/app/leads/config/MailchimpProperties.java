package com.app.leads.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.*;

@Data
@ConfigurationProperties(prefix = "mailchimp")
public class MailchimpProperties {
    private String apiKey;
    private String serverPrefix;
    private String audienceId;


}
