package br.my.company.keycloak.storage.person.client;

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

import br.my.company.keycloak.storage.person.model.Person;
import br.my.company.keycloak.storage.rest.client.PATCH;

@Path("/persons")
public interface PersonService {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	Response getAll(@QueryParam("filter") String filters);
	
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	Response getById(@PathParam("id") Long id);
	
	@GET
	@Path("/count")
	@Produces({ MediaType.APPLICATION_JSON })
	Response count(@QueryParam("where") String condition);
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response addPerson(Person person);
	
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response updatePerson(Person person);
	
	@PATCH
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response updateParcialPerson(@PathParam("id") Long id, Person person);
	
	@DELETE
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	Response removePerson(@PathParam("id") Long id);

}
