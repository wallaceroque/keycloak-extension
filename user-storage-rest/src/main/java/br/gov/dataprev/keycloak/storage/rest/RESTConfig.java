package br.gov.dataprev.keycloak.storage.rest;

public class RESTConfig {
	
	public String getConnectionUrl() {
        return System.getProperty("SIAC_CONNECTION_URL");
    }

}
