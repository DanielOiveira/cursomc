package com.danieloliveira.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danieloliveira.cursomc.domain.Cidade;
import com.danieloliveira.cursomc.domain.Cliente;
import com.danieloliveira.cursomc.domain.Endereco;
import com.danieloliveira.cursomc.domain.enums.TipoCliente;
import com.danieloliveira.cursomc.dto.ClienteDTO;
import com.danieloliveira.cursomc.dto.ClienteNewDTO;
import com.danieloliveira.cursomc.repositories.ClienteRepository;
import com.danieloliveira.cursomc.repositories.EnderecoRepository;
import com.danieloliveira.cursomc.services.exceptions.DataIntegrityException;
import com.danieloliveira.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj  = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Cliente não encontrado" + Cliente.class.getName())); 
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		 obj = repository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repository.save(newObj);
	}
	
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			repository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
		
	}
	
	public List<Cliente> findAll(){
		return repository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
				orderBy);
		
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDto(ClienteDTO objDto) {
		return new Cliente(
				objDto.getId(),
				objDto.getNome(),
				objDto.getEmail(),
				null,
				null);
	}
	
	public Cliente fromDto(ClienteNewDTO objDto) {
		Cliente  cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if(objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if(objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}

	

}
