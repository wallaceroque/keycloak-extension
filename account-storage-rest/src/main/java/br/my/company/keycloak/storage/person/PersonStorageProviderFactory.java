package br.my.company.keycloak.storage.person;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.storage.UserStorageProviderFactory;

import br.my.company.keycloak.storage.person.model.Person;
import br.my.company.keycloak.storage.rest.RESTIdentityStore;

/**
 * @author <a href="mailto:wallacerock@gmail.com">Wallace Roque</a>
 * @version $Revision: 1 $
 */
public class PersonStorageProviderFactory implements 
	UserStorageProviderFactory<PersonStorageProvider>,
	ServerInfoAwareProviderFactory {
	
    private static final Logger logger = Logger.getLogger(PersonStorageProviderFactory.class);
    
    private PersonIdentityStoreRegistry restStoreRegistry;
    RESTIdentityStore<Person>identityStore;

    @Override
    public PersonStorageProvider create(KeycloakSession session, ComponentModel model) {
    	try {
    		this.identityStore = this.restStoreRegistry.getRestStore(session, model);
            PersonStorageProvider provider = new PersonStorageProvider(this, session, model, (PersonIdentityStore)this.identityStore);
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("Versão", "1.0.0-SNAPSHOT");
        //info.put("Endereço da API REST", identityStore.getConfig().getConnectionUrl() + "");
        return info;
    }

    @Override
    public String getId() {
        return "Person Storage Provider";
    }

    @Override
    public String getHelpText() {
        return "Provider de usuários integrados a base de dados do SIAC";
    }
    
    @Override
    public void init(Config.Scope config) {
        this.restStoreRegistry = new PersonIdentityStoreRegistry();
    }

    @Override
    public void close() {
        logger.info("<<<<<< Closing factory");
        this.restStoreRegistry = null;

    }
}
