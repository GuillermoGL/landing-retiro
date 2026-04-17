package com.app.leads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.app.leads.config.MailchimpProperties;

@SpringBootApplication
@EnableConfigurationProperties(MailchimpProperties.class)
public class LeadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadsApplication.class, args);
	}

}
