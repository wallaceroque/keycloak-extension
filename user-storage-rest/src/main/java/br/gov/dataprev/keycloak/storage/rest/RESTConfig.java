package br.gov.dataprev.keycloak.storage.rest;

import javax.enterprise.inject.Default;

@Default
public class RESTConfig {
	
	public String getConnectionUrl() {
        return System.getProperty("SIAC_CONNECTION_URL");
    }

}
