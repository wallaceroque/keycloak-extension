package br.my.company.keycloak.storage.person;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

import br.my.company.keycloak.storage.rest.RESTConfig;

public class PersonIdentityStoreRegistry {
	
	private static final Logger logger = Logger.getLogger(PersonIdentityStoreRegistry.class);

    private Map<String, PersonIdentityStoreContext> restStores = new ConcurrentHashMap<>();

    public PersonIdentityStore getRestStore(KeycloakSession session, ComponentModel restModel) {
        PersonIdentityStoreContext context = restStores.get(restModel.getId());

        //MultivaluedHashMap<String, String> configModel = restModel.getConfig();
        RESTConfig restConfig = new RESTConfig();
        
        if (context == null || !restConfig.equals(context.config)) {
            logRESTConfig(session, restModel, restConfig);

            PersonIdentityStore store = createRestIdentityStore(restConfig);
            
            context = new PersonIdentityStoreContext(restConfig, store);
            restStores.put(restModel.getId(), context);
        }
        return context.store;
    }

    // Don't log REST password
    private void logRESTConfig(KeycloakSession session, ComponentModel restModel, RESTConfig restConfig) {
        logger.infof("Creating new REST Store for the REST storage provider: '%s', REST Configuration: %s", restModel.getName(), restConfig.toString());

        if (logger.isDebugEnabled()) {
            RealmModel realm = session.realms().getRealm(restModel.getParentId());
            List<ComponentModel> mappers = realm.getComponents(restModel.getId());
            mappers.stream().forEach((ComponentModel c) -> {

                logger.debugf("Mapper for provider: %s, Mapper name: %s, Provider: %s, Mapper configuration: %s", restModel.getName(), c.getName(), c.getProviderId(), c.getConfig().toString());

            });
        }
    }

    /**
     * Create PersonIdentityStore to be cached in the local registry
     */
    public static PersonIdentityStore createRestIdentityStore(RESTConfig config) {
    	checkSystemProperty("SIAC_CONNECTION_URL", "http://siac/api");

        return new PersonIdentityStore(config);
    }

    private static void checkSystemProperty(String name, String defaultValue) {
    	if (System.getProperty(name) == null) {
    		System.setProperty(name, defaultValue);
    	}
    	
    	if (System.getenv(name) != null) {
			System.setProperty(name, System.getenv(name));
		}
    	
    }

    private class PersonIdentityStoreContext {

        private PersonIdentityStoreContext(RESTConfig config, PersonIdentityStore store) {
            this.config = config;
            this.store = store;
        }

        private RESTConfig config;
        private PersonIdentityStore store;
    }

}
