package br.gov.dataprev.keycloak.storage.cidadao;

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

import br.gov.dataprev.keycloak.storage.rest.RESTConfig;
import br.gov.dataprev.keycloak.storage.rest.RESTIdentityStore;

public class CidadaoIdentityStore implements RESTIdentityStore<Cidadao> {
	
	private static final Logger logger = Logger.getLogger(CidadaoIdentityStore.class);
	
	private RESTConfig config;
	private WebTarget api;
	
	public CidadaoIdentityStore(RESTConfig config) {
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
	public Cidadao searchById(Long cpf) {
		Cidadao cidadao = null;
		Response response = null;
		try {
			response = this.api.path("cidadaos/{cpf}")
					.resolveTemplate("cpf", cpf.toString())
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			if (response == null) {
				throw new RuntimeException("Ocorre um problema ao processar a requisição");
			}
			
			if (response.getStatus() == 404) {
				throw new NotFoundException("Não foi possível consultar pelo CPF: " + cpf);
			}
			
			if (response.getStatus() == 200) {
				cidadao = response.readEntity(Cidadao.class);
			}
			
		} catch (Exception e) {
			throw new ModelException("Não foi possível encontrar o CPF: " + cpf, e);
			//throw new RuntimeException(e);
		} finally {
			if (response != null) response.close();
		}
		
		return cidadao;
	}
	
	public Cidadao searchByEmail(String email) {
		Cidadao cidadao = null;
		Response response = null;
		
		try {
			
			// String filter = "?filter[where][and][0][email][regexp]=/" + email + "/i";
			response = this.api.path("cidadaos")
					.queryParam("filter[where][and][0][email][regexp]","/" + email + "/i")
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			if (response.getStatus() == 404) {
				throw new NotFoundException("Não foi possível consultar pelo CPF: ");
			}
			
			if (response.getStatus() == 200) {
				cidadao = response.readEntity(Cidadao.class);
			}
			
		} catch (NotFoundException e) {
			logger.info("Não foi possível encontrar o usuário de email: " + email);
			
		} catch (Exception e) {
			throw new ModelException("Não foi possível encontrar o usuário de email: " + email, e);
		} finally {
			if (response != null) response.close();
		}
		
		return cidadao;
		
	}

	@Override
	public void add(Cidadao entity) {
	}
	
	@Override
	public void remove(Cidadao entidade) {
	}

	@Override
	public void update(Cidadao entity) {
		Response response = null;
		
		try {
			//service.updateParcialCidadao(entity.getCpf(), entity);
			response = this.api.path("cidadaos/{cpf}")
					.resolveTemplate("cpf", entity.getCpf().toString())
					.request(MediaType.APPLICATION_JSON)
					.build("PATCH", Entity.json(entity))
					.invoke();
			
			if (response.getStatus() != 200) {
				throw new BadRequestException(response);
			}
		} catch(Exception e) {
			throw new ModelException("Não foi possível atualizar o usuário de CPF: " + entity.getCpf() , e);
		} finally {
			if (response != null) response.close();
		}
	}

	@Override
	public void validatePassword(Cidadao entity, String senha) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

}
