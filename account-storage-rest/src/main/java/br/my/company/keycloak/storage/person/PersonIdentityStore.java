package br.my.company.keycloak.storage.person;

import java.util.List;

import javax.naming.AuthenticationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jboss.logging.Logger;

import br.my.company.keycloak.storage.person.model.Person;
import br.my.company.keycloak.storage.rest.RESTConfig;
import br.my.company.keycloak.storage.rest.RESTIdentityStore;

public class PersonIdentityStore implements RESTIdentityStore<Person> {
	
	private static final Logger logger = Logger.getLogger(PersonIdentityStore.class);
	
	private RESTConfig config;
	private WebTarget api;
	
	public PersonIdentityStore(RESTConfig config) {
        this.config = config;
        
        logger.info("SIAC_CONNECTION_URL " + config.getConnectionUrl());
        
        Client client = ClientBuilder.newClient();
        this.api = client.target(UriBuilder.fromPath(config.getConnectionUrl()));
    }

	@Override
	public RESTConfig getConfig() {
		return this.config;
	}
	
	@Override
	public Person searchById(Long id) throws NotFoundException, RuntimeException {
		Person person = null;
		Response response = null;
		try {
			response = this.api.path("persons/{id}")
					.resolveTemplate("id", id.toString())
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			if (response == null) {
				throw new RuntimeException("The was a problem processing your request.");
			}
			
			if (response.getStatus() == 404) {
				throw new NotFoundException("Person not found. ID: " + id);
			}
			
			if (response.getStatus() == 200) {
				person = response.readEntity(Person.class);
			}
			
		} catch (NullPointerException | ProcessingException e) {
			throw new RuntimeException(e);
		} finally {
			if (response != null) response.close();
		}
		
		return person;
	}
	
	@Override
	public Person searchByEmail(String email) throws NotFoundException, RuntimeException {
		List<Person> persons = null;
		Response response = null;
		
		try {
			response = this.api.path("persons")
					.queryParam("filter[where][and][0][email][regexp]","/" + email + "/i")
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			if (response == null) {
				throw new RuntimeException("The was a problem processing your request.");
			}
			
			if (response.getStatus() == 200) {
				persons = response.readEntity(new GenericType<List<Person>>() {});
			}
			
			if (persons.size() == 0) {
				throw new NotFoundException("Person not found. E-mail: " + email);
			}
			
			if(persons.size() > 1) {
				throw new RuntimeException("Multiples users with the same email.");
			}
			
		} catch (NullPointerException | ProcessingException e) {
			throw new RuntimeException(e);
		} finally {
			if (response != null) response.close();
		}
		
		return persons.get(0);
		
	}

	@Override
	public void add(Person entity) {
	}
	
	@Override
	public void remove(Person entidade) {
	}

	@Override
	public void update(Person entity) throws BadRequestException, RuntimeException {
		Response response = null;
		
		try {
			response = this.api.path("persons/{id}")
					.resolveTemplate("id", entity.getId().toString())
					.request(MediaType.APPLICATION_JSON)
					.build("PATCH", Entity.json(entity))
					.invoke();
			
			if (response.getStatus() != 200) {
				throw new BadRequestException(response);
			}
		} catch(NullPointerException | ProcessingException e) {
			throw new RuntimeException("The was a problem processing your request. ID: " + entity.getId() , e);
		} finally {
			if (response != null) response.close();
		}
	}

	@Override
	public void validatePassword(Person entity, String senha) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

}
