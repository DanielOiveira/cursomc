package com.danieloliveira.cursomc.services;


import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danieloliveira.cursomc.domain.Cliente;
import com.danieloliveira.cursomc.domain.ItemPedido;
import com.danieloliveira.cursomc.domain.PagamentoComBoleto;
import com.danieloliveira.cursomc.domain.Pedido;
import com.danieloliveira.cursomc.domain.enums.EstadoPagamento;
import com.danieloliveira.cursomc.repositories.ItemPedidoRepository;
import com.danieloliveira.cursomc.repositories.PagamentoRepository;
import com.danieloliveira.cursomc.repositories.PedidoRepository;
import com.danieloliveira.cursomc.security.UserSS;
import com.danieloliveira.cursomc.services.exceptions.AuthorizationException;
import com.danieloliveira.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository ipRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj  = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Pedido não encontrado" + Pedido.class.getName())); 
	} 
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		ipRepository.saveAll(obj.getItens());
		
		emailService.sendOrderConfirmationHtmlEmail(obj);
		
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest =PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
				orderBy);
		
		Cliente cliente = clienteService.find(user.getId());
		return repository.findByCliente(cliente, pageRequest);
	}
}
