package com.danieloliveira.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danieloliveira.cursomc.domain.Pedido;
import com.danieloliveira.cursomc.repositories.PedidoRepository;
import com.danieloliveira.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;

	public Pedido buscar(Integer id) {
		Optional<Pedido> obj  = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Pedido não encontrado" + Pedido.class.getName())); 
	} 
}
