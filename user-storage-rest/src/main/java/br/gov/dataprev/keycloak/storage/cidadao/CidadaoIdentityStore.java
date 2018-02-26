package br.gov.dataprev.keycloak.storage.cidadao;

import java.util.Map;

import javax.naming.AuthenticationException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ClientResponse;
import org.keycloak.models.ModelException;

import br.gov.dataprev.keycloak.storage.rest.RESTConfig;
import br.gov.dataprev.keycloak.storage.rest.RESTIdentityStore;

public class CidadaoIdentityStore implements RESTIdentityStore<Cidadao> {
	
	private static final Logger logger = Logger.getLogger(CidadaoIdentityStore.class);
	
	private RESTConfig config;
	private ResteasyWebTarget serviceTarget;
	private CidadaoService service;
	
	public CidadaoIdentityStore(RESTConfig config) {
        this.config = config;
        
        logger.info("SIAC_CONNECTION_URL " + config.getConnectionUrl());
        
        ResteasyClient client = new ResteasyClientBuilder().build();
        this.setServiceTarget(client.target(UriBuilder.fromPath(config.getConnectionUrl())));
    }

	@Override
	public RESTConfig getConfig() {
		return this.config;
	}
	
	@Override
	public void setService(CidadaoService service) {
		this.service = service;
	}
	
	@Override
	public Cidadao searchById(Long cpf) {
		Cidadao cidadao = null;
		Response response = null;
		try {
			response = (ClientResponse)service.getByCpf(cpf);
			
			if (response.getStatus() == 404) {
				throw new NotFoundException("Não foi possível consultar pelo CPF: ");
			}
			
			if (response.getStatus() == 200) {
				cidadao = response.readEntity(Cidadao.class);
			}
			
		} catch (NotFoundException e) {
			logger.info("Não foi possível encontrar o CPF: " + cpf);
			
		} catch (Exception e) {
			throw new ModelException("Não foi possível encontrar o CPF: " + cpf, e);
		} finally {
			response.close();
		}
		
		return cidadao;
	}
	
	public Cidadao searchByEmail(String email) {
		Cidadao cidadao = null;
		Response response = null;
		
		try {
			
			String filter = "filter[where][and][0][email][regexp]=/" + email + "/i";
			response = (ClientResponse)service.getAll(filter);
			
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
			response.close();
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
		try {
			service.updateParcialCidadao(entity.getCpf(), entity);
		} catch(Exception e) {
			throw new ModelException("Não foi possível atualizar o usuário de CPF: " + entity.getCpf() , e);
		}
	}

	@Override
	public void validatePassword(Cidadao entity, String senha) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	public ResteasyWebTarget getServiceTarget() {
		return serviceTarget;
	}

	public void setServiceTarget(ResteasyWebTarget serviceTarget) {
		this.serviceTarget = serviceTarget;
	}

}
