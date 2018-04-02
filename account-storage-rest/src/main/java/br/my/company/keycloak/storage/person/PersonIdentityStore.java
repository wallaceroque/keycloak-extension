package br.my.company.keycloak.storage.person;

import javax.naming.AuthenticationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jboss.logging.Logger;
import org.keycloak.models.ModelException;

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
	public Person searchById(Long id) {
		Person person = null;
		Response response = null;
		try {
			response = this.api.path("persons/{id}")
					.resolveTemplate("id", id.toString())
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			if (response == null) {
				throw new RuntimeException("Ocorre um problema ao processar a requisição");
			}
			
			if (response.getStatus() == 404) {
				throw new NotFoundException("Não foi possível consultar pelo ID: " + id);
			}
			
			if (response.getStatus() == 200) {
				person = response.readEntity(Person.class);
			}
			
		} catch (Exception e) {
			throw new ModelException("Não foi possível encontrar o ID: " + id, e);
			//throw new RuntimeException(e);
		} finally {
			if (response != null) response.close();
		}
		
		return person;
	}
	
	public Person searchByEmail(String email) {
		Person person = null;
		Response response = null;
		
		try {
			
			// String filter = "?filter[where][and][0][email][regexp]=/" + email + "/i";
			response = this.api.path("persons")
					.queryParam("filter[where][and][0][email][regexp]","/" + email + "/i")
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			if (response.getStatus() == 404) {
				throw new NotFoundException("Não foi possível consultar pelo ID: ");
			}
			
			if (response.getStatus() == 200) {
				person = response.readEntity(Person.class);
			}
			
		} catch (NotFoundException e) {
			logger.info("Não foi possível encontrar o usuário de email: " + email);
			
		} catch (Exception e) {
			throw new ModelException("Não foi possível encontrar o usuário de email: " + email, e);
		} finally {
			if (response != null) response.close();
		}
		
		return person;
		
	}

	@Override
	public void add(Person entity) {
	}
	
	@Override
	public void remove(Person entidade) {
	}

	@Override
	public void update(Person entity) {
		Response response = null;
		
		try {
			//service.updateParcialPerson(entity.getId(), entity);
			response = this.api.path("persons/{id}")
					.resolveTemplate("id", entity.getId().toString())
					.request(MediaType.APPLICATION_JSON)
					.build("PATCH", Entity.json(entity))
					.invoke();
			
			if (response.getStatus() != 200) {
				throw new BadRequestException(response);
			}
		} catch(Exception e) {
			throw new ModelException("Não foi possível atualizar o usuário de ID: " + entity.getId() , e);
		} finally {
			if (response != null) response.close();
		}
	}

	@Override
	public void validatePassword(Person entity, String senha) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

}
