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
import org.keycloak.models.ModelException;
import org.keycloak.services.messages.Messages;

import br.my.company.keycloak.storage.person.model.Person;
import br.my.company.keycloak.storage.rest.RESTConfig;
import br.my.company.keycloak.storage.rest.RESTIdentityStore;
import br.my.company.keycloak.storage.rest.model.ErrorResponse;

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
			
			response.bufferEntity();
			
			person = response.readEntity(Person.class);
		
		} catch (NullPointerException | BadRequestException | ProcessingException e) {
			logger.error("Invalid Request", e);
			RuntimeException wrapper = new RuntimeException(Errors.INVALID_REQUEST, e);
			throw new ModelException(Messages.COULD_NOT_PROCEED_WITH_AUTHENTICATION_REQUEST, wrapper);
//			throw new UserStorageException(
//					Errors.INVALID_REQUEST,
//					"Invalid Request",
//					Messages.UNEXPECTED_ERROR_HANDLING_REQUEST,
//					"We were unable to process your request.");
			
		} catch (NotFoundException nfe) {
			logger.warn("Person not found. ID: " + id, nfe);
			return null;
			
		} catch (ClientErrorException cee) {
			logger.error("Response exception 4xx: status code: " + response.getStatus(), cee);
			ErrorResponse error = response.readEntity(ErrorResponse.class);
			RuntimeException wrapper = new RuntimeException(error.getMessage(), cee);
			throw new ModelException(Messages.USER_STORAGE_PROVIDER_LOGIN_FAILURE, wrapper);
//			throw new UserStorageException(
//					Errors.INVALID_REQUEST,
//					"The user storage provider was unable to process request",
//					Messages.USER_STORAGE_PROVIDER_LOGIN_FAILURE,
//					error.getMessage());
			
		} catch (ServiceUnavailableException sue) {
			logger.error("Response exception 503: " + sue.getMessage(), sue);
			RuntimeException wrapper = new RuntimeException(Errors.IDENTITY_PROVIDER_ERROR, sue);
			throw new ModelException(Messages.USER_STORAGE_PROVIDER_UNAVAILABLE, wrapper);
//			throw new UserStorageException(
//					Errors.IDENTITY_PROVIDER_ERROR,
//					"Error to request the user storage provider.",
//					Messages.USER_STORAGE_PROVIDER_UNAVAILABLE,
//					"The servers are unavailable. Try later.");
		
		} catch (ServerErrorException see) {
			logger.error("Response exception 500. A unexpected error occurred", see);
			RuntimeException wrapper = new RuntimeException(Errors.IDENTITY_PROVIDER_ERROR, see);
			throw new ModelException(Messages.USER_STORAGE_PROVIDER_UNEXPECTED_ERROR, wrapper);
//			throw new UserStorageException(
//					Errors.IDENTITY_PROVIDER_ERROR,
//					"Error to request the user storage provider.",
//					Messages.USER_STORAGE_PROVIDER_UNEXPECTED_ERROR,
//					"I'm sorry, an unexpected error occurred. We are working to resolve it.");
		}
		
		finally {
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
				throw new BadRequestException("The was a problem processing your request.");
			}
			
			if (response.getStatus() == 200) {
				persons = response.readEntity(new GenericType<List<Person>>() {});
			}
			
			if (persons.size() == 0) {
				throw new NotFoundException("Person not found. E-mail: " + email);
			}
			
			if(persons.size() > 1) {
				throw new BadRequestException("Multiples users with the same email.");
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
