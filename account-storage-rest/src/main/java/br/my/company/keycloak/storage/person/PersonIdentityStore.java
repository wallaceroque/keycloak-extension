package br.my.company.keycloak.storage.person;

import java.util.List;

import javax.naming.AuthenticationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jboss.logging.Logger;
import org.keycloak.events.Errors;
import org.keycloak.services.messages.Messages;

import br.my.company.keycloak.storage.person.model.Person;
import br.my.company.keycloak.storage.rest.RESTConfig;
import br.my.company.keycloak.storage.rest.RESTIdentityStore;
import br.my.company.keycloak.storage.rest.RESTIdentityStoreException;
import br.my.company.keycloak.storage.rest.model.ErrorResponse;

public class PersonIdentityStore implements RESTIdentityStore<Person> {
	
	private static final Logger logger = Logger.getLogger(PersonIdentityStore.class);
	
	private RESTConfig config;
	private WebTarget api;
	
	public PersonIdentityStore(RESTConfig config) {
        this.config = config;
        
        logger.info("SI_CONNECTION_URL " + config.getConnectionUrl());
        
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
	
			response.bufferEntity();
			
			person = response.readEntity(Person.class);
		
		} catch (NullPointerException | ProcessingException e) {
			logger.error("We were unable to process your request.", e);
			RuntimeException wrapper = new RuntimeException(Errors.INVALID_REQUEST, e);
			throw new RESTIdentityStoreException(Messages.UNEXPECTED_ERROR_HANDLING_REQUEST, wrapper);
			
		} catch (NotFoundException nfe) {
			logger.warn("Person not found. ID: " + id, nfe);
			return null;
			
		} catch (ClientErrorException cee) {
			logger.error("The user storage provider was unable to process request. Status code: " + response.getStatus(), cee);
			ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
			String message = (errorResponse != null && errorResponse.getMessage().equals("")) ? errorResponse.getMessage() : "userStorageProviderLoginFailure";
			RuntimeException wrapper = new RuntimeException(Errors.INVALID_REQUEST, cee);
			throw new RESTIdentityStoreException(message, wrapper);
			
		} catch (ServiceUnavailableException sue) {
			logger.error("The servers are unavailable: " + sue.getMessage(), sue);
			RuntimeException wrapper = new RuntimeException(Errors.IDENTITY_PROVIDER_ERROR, sue);
			throw new RESTIdentityStoreException("userStorageProviderUnavailable", wrapper);
		
		} catch (ServerErrorException see) {
			logger.error("I'm sorry, an unexpected error occurred. We are working to resolve it.", see);
			RuntimeException wrapper = new RuntimeException(Errors.IDENTITY_PROVIDER_ERROR, see);
			throw new RESTIdentityStoreException("userStorageProviderUnexpectedError", wrapper);
			
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
			response.bufferEntity();
			
			persons = response.readEntity(new GenericType<List<Person>>() {});
			
			if (persons.size() == 0) {
				logger.warn("Person not found. E-mail: " + email);
				return null;
			}
			
			if(persons.size() > 1) {
				logger.warn("Multiples users with the same email.");
			}
			
		} catch (NullPointerException | BadRequestException | ProcessingException e) {
			logger.error("We were unable to process your request.", e);
			RuntimeException wrapper = new RuntimeException(Errors.INVALID_REQUEST, e);
			throw new RESTIdentityStoreException(Messages.COULD_NOT_PROCEED_WITH_AUTHENTICATION_REQUEST, wrapper);
			
		} catch (NotFoundException nfe) {
			logger.warn("person not found by e-mail informed: " + email, nfe);
			return null;
			
		} catch (ClientErrorException cee) {
			logger.error("The user storage provider was unable to process request. Status code: " + response.getStatus(), cee);
			ErrorResponse error = response.readEntity(ErrorResponse.class);
			RuntimeException wrapper = new RuntimeException(error.getMessage(), cee);
			throw new RESTIdentityStoreException("userStorageProviderLoginFailure", wrapper);
			
		} catch (ServiceUnavailableException sue) {
			logger.error("The servers are unavailable: " + sue.getMessage(), sue);
			RuntimeException wrapper = new RuntimeException(Errors.IDENTITY_PROVIDER_ERROR, sue);
			throw new RESTIdentityStoreException("userStorageProviderUnavailable", wrapper);
		
		} catch (ServerErrorException see) {
			logger.error("I'm sorry, an unexpected error occurred. We are working to resolve it.", see);
			RuntimeException wrapper = new RuntimeException(Errors.IDENTITY_PROVIDER_ERROR, see);
			throw new RESTIdentityStoreException("userStorageProviderUnexpectedError", wrapper);
			
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
