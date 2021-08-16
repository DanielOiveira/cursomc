package com.danieloliveira.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danieloliveira.cursomc.domain.Cliente;
import com.danieloliveira.cursomc.repositories.ClienteRepository;
import com.danieloliveira.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj  = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Cliente não encontrado" + Cliente.class.getName())); 
	} 
	

}
