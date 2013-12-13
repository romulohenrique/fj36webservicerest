package br.com.caelum.payfast.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.caelum.payfast.modelo.Pagamento;
import br.com.caelum.payfast.modelo.Transacao;

@Path("/v1/pagamento")
@Stateless
public class PagamentoResource {

	private static Map<Integer,Pagamento> REPO = new HashMap<>();
	private static Integer idPagamento = 1;

	// https://developer.paypal.com/docs/api/
	// https://developer.paypal.com/
	
	public PagamentoResource() {
		Pagamento pagamento = new Pagamento();
		pagamento.setId(idPagamento++);
		pagamento.setValor(BigDecimal.TEN);
		REPO.put(pagamento.getId(), pagamento);
	}
		
	@POST
	@Consumes({"application/json"})
	public Response criarPagamento(Transacao transacao) throws URISyntaxException  {

		Pagamento pagamento = new Pagamento();
		pagamento.setId(nextId());
		pagamento.setValor(transacao.getValor());
		pagamento.comStatusCriado();
		
		REPO.put(pagamento.getId(), pagamento);

		System.out.println("PAGAMENTO CRIADO " + pagamento);
		return Response.created(new URI("/pagamento/" + pagamento.getId())).entity(pagamento)
				.type(MediaType.APPLICATION_JSON).build();
	}

	private Integer nextId() {
		return idPagamento++;
	}

	@PUT
	@Path("/{id}")
	@Produces("application/json") //cuidado javax.ws.rs
	public Pagamento confirmarPagamento(@PathParam("id") Integer pagamentoId) {
		Pagamento pagamento = REPO.get(pagamentoId);
		pagamento.comStatusConfirmado();
		System.out.println("Pagamento confirmado: " + pagamento);
		return pagamento;
	}

	@GET
	@Path("/{id}")
	@Produces({"application/json", "application/xml"}) 
	public Pagamento buscaPagamento(@PathParam("id") Integer id) {
		return REPO.get(id);
	}

	
}
