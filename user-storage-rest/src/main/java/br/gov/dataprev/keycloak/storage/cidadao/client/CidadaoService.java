package br.gov.dataprev.keycloak.storage.cidadao.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.dataprev.keycloak.storage.cidadao.model.Cidadao;
import br.gov.dataprev.keycloak.storage.rest.client.PATCH;

@Path("/cidadaos")
public interface CidadaoService {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	Response getAll(@QueryParam("filter") String filtros);
	
	@GET
	@Path("/{cpf}")
	@Produces({ MediaType.APPLICATION_JSON })
	Response getByCpf(@PathParam("cpf") Long cpf);
	
	@GET
	@Path("/count")
	@Produces({ MediaType.APPLICATION_JSON })
	Response count(@QueryParam("where") String condicao);
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response addCidadao(Cidadao cidadao);
	
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response updateCidadao(Cidadao cidadao);
	
	@PATCH
	@Path("/{cpf}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response updateParcialCidadao(@PathParam("cpf") Long cpf, Cidadao cidadao);
	
	@DELETE
	@Path("/{cpf}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response removeCidadao(@PathParam("cpf") Integer cpf);

}
