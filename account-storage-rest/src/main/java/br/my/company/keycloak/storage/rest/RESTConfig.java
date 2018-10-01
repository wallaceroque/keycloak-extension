package br.my.company.keycloak.storage.rest;

public class RESTConfig {
	
	public String getConnectionUrl() {
        return System.getProperty("SI_CONNECTION_URL");
    }

}
