package br.my.company.keycloak.storage.rest;

import javax.naming.AuthenticationException;

import br.my.company.keycloak.storage.rest.model.Entity;

public interface RESTIdentityStore<T extends Entity> {
	
	/**
     * Returns configuration of instance's REST Identity Store
     *
     * @return RESTConfig
     */
    RESTConfig getConfig();
    
    /**
     * 
     * @param id 
     * @return Entity
     */
    T searchById(Long id);
    
    /**
     * 
     * @param email
     * @return Entity
     */
    T searchByEmail(String email);
	
	/**
     * Persists an entity
     *
     * @param entity
     */
	void add(T entity);
	
	/**
     * Updates an entity
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
     * Returns a entities' list from a query
     * 
     * @param restQuery
     * @return 
     */
    //List<T> fetchQueryResults(RESTQuery restQuery);
    
    /**
     * Returns a number of records founded from a query
     * @param restQuery
     * @return
     */
    //int countQueryResults(RESTQuery restQuery);
    
    /**
     * Validate a specific credential.
     *
     * @param entity Usuário Keycloak
     * @param senha Senha do usuário 
     * @throws AuthenticationException caso ocorra falha de autenticação
     */
    void validatePassword(T entity, String senha) throws AuthenticationException;

}
