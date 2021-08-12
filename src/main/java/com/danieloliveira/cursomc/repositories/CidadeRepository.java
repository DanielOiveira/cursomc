package com.danieloliveira.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danieloliveira.cursomc.domain.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Integer>{

}
