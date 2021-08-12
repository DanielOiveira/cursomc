package com.danieloliveira.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danieloliveira.cursomc.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
