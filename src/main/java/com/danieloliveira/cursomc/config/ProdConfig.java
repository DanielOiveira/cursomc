package com.danieloliveira.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.danieloliveira.cursomc.services.DBService;
import com.danieloliveira.cursomc.services.EmailService;
import com.danieloliveira.cursomc.services.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instantiateDataBase() throws ParseException {
		
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateTestDataBase();
		return true;
	}
	
	@Bean
	public EmailService emailSerice() {
		return new SmtpEmailService();
	}
}
