
package br.gov.dataprev.keycloak.storage.cidadao;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ModelException;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.credential.PasswordUserCredentialModel;
import org.keycloak.policy.PasswordPolicyManagerProvider;
import org.keycloak.policy.PolicyError;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Stateful
@Local(CidadaoStorageProvider.class)
public class CidadaoStorageProvider implements 
		UserStorageProvider,
		UserLookupProvider,
		CredentialInputValidator,
		CredentialInputUpdater
		//UserQueryProvider,
		//CredentialInputUpdater,
//		CredentialInputValidator,
//		CredentialAuthentication,
//		ImportedUserValidation
        // OnUserCache
{
    private static final Logger logger = Logger.getLogger(CidadaoStorageProvider.class);
    public static final String PASSWORD_CACHE_KEY = UserAdapter.class.getName() + ".password";

    protected ComponentModel model;
    protected KeycloakSession session;
    
    protected CidadaoStorageProviderFactory factory;
    //protected UserStorageProviderModel model;
    protected CidadaoIdentityStore identityStore;
    //protected CidadaoService service;
    //protected PasswordUpdateCallback updater;
    //protected LDAPStorageMapperManager mapperManager;
    protected CidadaoStorageUserManager userManager;
    
    protected final Set<String> supportedCredentialTypes = new HashSet<>();
    
    /*public CidadaoStorageProvider(CidadaoStorageProviderFactory factory, KeycloakSession session, ComponentModel model, CidadaoIdentityStore identityStore) {
    	logger.debug("provider: constructor");
        this.factory = factory;
        this.session = session;
        this.identityStore = identityStore;
        //this.mapperManager = new LDAPStorageMapperManager(this);
        this.userManager = new CidadaoStorageUserManager(this);

        supportedCredentialTypes.add(UserCredentialModel.PASSWORD);
    }*/
    
    @PostConstruct
    public void init() {
    	logger.info("provider: init");
        this.userManager = new CidadaoStorageUserManager(this);
        supportedCredentialTypes.add(UserCredentialModel.PASSWORD);
    }

    public void setModel(ComponentModel model) {
        this.model = model;
    }

    public void setSession(KeycloakSession session) {
        this.session = session;
    }
    
    //// UserStorageProvider
    
    @Remove
    @Override
	public void close() {
		// TODO Auto-generated method stub	
	}
    
    //// UserLookupProvider
    
    @Override
	public UserModel getUserById(String keycloakId, RealmModel realm) {
    	logger.info("getUserById: keycloakId " + keycloakId);
    	/*UserModel alreadyLoadedInSession = userManager.getManagedProxiedUser(keycloakId);
        if (alreadyLoadedInSession != null) {
        	logger.info("getUserById: usuário já está carregado na sessão");
        	return alreadyLoadedInSession;
        }*/

        StorageId storageId = new StorageId(keycloakId);
        return getUserByUsername(storageId.getExternalId(), realm);
	}
    
    @Override
	public UserModel getUserByUsername(String cpf, RealmModel realm) {
    	logger.info("getUserByUsername: cpf: " + cpf);
    	Cidadao cidadao = identityStore.searchById(Long.valueOf(cpf));
    	
         if (cidadao == null) {
             return null;
         }
         
         UserAdapter adapter = new UserAdapter(session, realm, model, cidadao);
         
         //userManager.setManagedProxiedUser(adapter, cidadao);

         return adapter;

         //return new UserAdapter(session, realm, model, cidadao);
	}
    
    @Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
    	logger.info("getUserByEmail: email: " + email);
    	Cidadao cidadao = identityStore.searchByEmail(email);
    	
         if (cidadao == null) {
             return null;
         }
         
         UserAdapter adapter = new UserAdapter(session, realm, model, cidadao);
         
         //userManager.setManagedProxiedUser(adapter, cidadao);

         return adapter;

         //return new UserAdapter(session, realm, model, cidadao);
	}
    
    
    // CredentialInputValidator
    
    @Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
    	return getSupportedCredentialTypes().contains(credentialType);
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
		if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
		logger.info("isValid: UserModel.getId() -> " + user.getId());
        //UserAdapter adapter = (UserAdapter)getUserByUsername(StorageId.externalId(user.getId()), realm);
		Cidadao cidadao = identityStore.searchById(Long.valueOf((StorageId.externalId(user.getId()))));
        //String password = getPassword(adapter);
		String password = cidadao.getSenha();
        UserCredentialModel cred = (UserCredentialModel)input;
        logger.info("isValid: input -> " + cred.getValue() + " user password -> " + password);
        return password != null && password.equals(cred.getValue());
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		return getSupportedCredentialTypes().contains(credentialType);
	}
	
	
	/// CredentialInputUpdater
	
	@Override
	public void disableCredentialType(RealmModel realm, UserModel user, String credential) {
		
	}

	@Override
	public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
		return Collections.EMPTY_SET;
	}

	/**
	 * O retorno deste método visa informar se as credenciais atualizadas(ou não) são do tipo password.
	 */
	@Override
	public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
		if (!CredentialModel.PASSWORD.equals(input.getType()) || ! (input instanceof PasswordUserCredentialModel)) return false;
		
		PasswordUserCredentialModel cred = (PasswordUserCredentialModel)input;
        String password = cred.getValue();
        PolicyError error = session.getProvider(PasswordPolicyManagerProvider.class).validate(realm, user, password);
		
        if (error != null) throw new ModelException(error.getMessage(), error.getParameters());
	    
        try {
        	Cidadao cidadao = identityStore.searchById(Long.valueOf((StorageId.externalId(user.getId()))));
	        cidadao.setSenha(password);
	        
	        identityStore.update(cidadao);
	
	        return true;
	    } catch (ModelException me) {
	    	throw me;
	    }
	}
	
	private boolean validPassword(RealmModel realm, UserModel user, String value) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Set<String> getSupportedCredentialTypes() {
        return new HashSet<String>(this.supportedCredentialTypes);
    }
    
    public UserAdapter getUserAdapter(UserModel user) {
        UserAdapter adapter = null;
        if (user instanceof CachedUserModel) {
            adapter = (UserAdapter)((CachedUserModel)user).getDelegateForUpdate();
        } else {
            adapter = (UserAdapter)user;
        }
        return adapter;
    }

    public String getPassword(UserModel user) {
        String password = null;
        if (user instanceof CachedUserModel) {
            password = (String)((CachedUserModel)user).getCachedWith().get(PASSWORD_CACHE_KEY);
        } else if (user instanceof UserAdapter) {
            password = ((UserAdapter)user).getPassword();
        }
        return password;
    }
    
    public void setIdentityStore(CidadaoIdentityStore restStore) {
    	CidadaoService service = restStore.getServiceTarget().proxy(CidadaoService.class);
    	restStore.setService(service);
    	
    	this.identityStore = restStore;
    }

}
