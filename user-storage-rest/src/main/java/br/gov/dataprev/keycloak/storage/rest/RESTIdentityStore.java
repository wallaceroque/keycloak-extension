package br.gov.dataprev.keycloak.storage.rest;

import javax.naming.AuthenticationException;

import br.gov.dataprev.keycloak.storage.cidadao.Cidadao;
import br.gov.dataprev.keycloak.storage.rest.model.Entity;

public interface RESTIdentityStore<T extends Entity> {
	
	/**
     * Retorna a configuração da instância do Identity Store baseado em REST
     *
     * @return
     */
    RESTConfig getConfig();
    
    /**
     * 
     * @param id 
     * @return Cidadao 
     */
    Cidadao searchById(Long id);
	
	/**
     * Persiste uma entidade
     *
     * @param entity
     */
	void add(T entity);
	
	/**
     * Atualiza uma entidade
     *
     * @param entity
     */
	void update(T entity);
	
	/**
     * Removes the specified IdentityType
     *
     * @param entity
     */
    void remove(T entidade);
    
    // Identity query

    /**
     * Retorna uma lista de entidades a partir de uma consulta
     * 
     * @param restQuery
     * @return 
     */
    //List<Entity> fetchQueryResults(RESTQuery restQuery);
    
    /**
     * Retorna a quantidade de registros encontrados a partir de uma consulta
     * @param restQuery
     * @return
     */
    //int countQueryResults(RESTQuery restQuery);
    
    /**
     * Valida uma credencial específica.
     *
     * @param entity Usuário Keycloak
     * @param senha Senha do usuário 
     * @throws AuthenticationException caso ocorra falha de autenticação
     */
    void validatePassword(T entity, String senha) throws AuthenticationException;

}
