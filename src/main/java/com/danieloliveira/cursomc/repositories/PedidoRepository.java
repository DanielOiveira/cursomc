package com.danieloliveira.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danieloliveira.cursomc.domain.Pedido;

public interface PedidoRepository  extends JpaRepository<Pedido, Integer>{

}
