package com.danieloliveira.cursomc.repositories;


import java.util.List;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.danieloliveira.cursomc.domain.Categoria;
import com.danieloliveira.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

	/*
	 * A consulta pode ser realizada com JPQL com auxilio da anotação Query ou com o nome definido de forma padronizada do Spring data :)
	 * 
	 * @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome") String name,@Param("categorias") List<Categoria> categorias, Pageable pageResquest);*/
	
	@Transactional(readOnly = true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String name,@Param("categorias") List<Categoria> categorias, Pageable pageResquest);
}
