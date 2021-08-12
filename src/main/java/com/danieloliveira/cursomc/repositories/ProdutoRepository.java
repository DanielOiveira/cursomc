package com.danieloliveira.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danieloliveira.cursomc.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

}
