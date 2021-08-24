package com.danieloliveira.cursomc.services;



import org.springframework.mail.SimpleMailMessage;

import com.danieloliveira.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
