
package br.gov.dataprev.keycloak.storage.cidadao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ModelException;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserModel.RequiredAction;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.credential.PasswordUserCredentialModel;
import org.keycloak.models.utils.UserModelDelegate;
import org.keycloak.policy.PasswordPolicyManagerProvider;
import org.keycloak.policy.PolicyError;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.federated.UserFederatedStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CidadaoStorageProvider implements 
		UserStorageProvider,
		UserLookupProvider,
		CredentialInputValidator,
		CredentialInputUpdater
		//UserQueryProvider,
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
    
    public CidadaoStorageProvider(CidadaoStorageProviderFactory factory, KeycloakSession session, ComponentModel model, CidadaoIdentityStore identityStore) {
    	logger.debug("provider: constructor");
        this.factory = factory;
        this.session = session;
        this.model = model;
        this.identityStore = identityStore;
        //this.mapperManager = new LDAPStorageMapperManager(this);
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
    	logger.info("getUserByUsername: count local storage: " + session.userLocalStorage().getUsersCount(realm));
    	logger.info("getUserByUsername: count federated storage: " + session.userFederatedStorage().getStoredUsersCount(realm));
    	Set<FederatedIdentityModel> models = session.userFederatedStorage().getFederatedIdentities(new StorageId(cpf).getId(), realm);
    	logger.info("getUserByUsername: count federated identity model stored: " + models.size());
    	
    	try{
    		List<UserModel> users = session.userLocalStorage().getUsers(realm);
            users.forEach(user -> 
            	logger.info("getUserByUsername: user from local storage: id: " + 
            			user.getId() + " username: " + user.getUsername()));
            
            List<String> federatedStoredUsers = session.userFederatedStorage().getStoredUsers(realm, 0, 5);
            
            federatedStoredUsers.forEach(user -> {
            	logger.info("getUserByUsername: user from federated storage: " + user);
	            session.userFederatedStorage()
	        		.getRequiredActions(realm, new StorageId(cpf).getId())
	        		.forEach(requiredAction -> 
	        			logger.info("getUserByUsername: required action from federated storage: " + requiredAction));
	            });
            
            ///////////////////////////////////////////////////////////
            
            Cidadao cidadao = identityStore.searchById(Long.valueOf(cpf));
            if (cidadao == null) {
                return null;
            }
            
            //UserModel userModel = session.userLocalStorage().getUserByUsername(cpf, realm);
            
            UserAdapter userAdapter = new UserAdapter(session, realm, model, cidadao);
            
            if (userAdapter.isEnabled()) {
            	logger.info("getUserByUsername: keycloakId:" + userAdapter.getId());
            	//session.userLocalStorage().addUser(realm, cpf);
            	if (cidadao.isPrimeiroLogin()) userAdapter.addRequiredAction(RequiredAction.UPDATE_PASSWORD);
            }
            
            //userManager.setManagedProxiedUser(adapter, cidadao);

            return userAdapter;
    		
    	} catch(ModelException re) {
    		logger.error("Um erro aconteceu e nada será feito: " + re.getMessage());
    		throw new RuntimeException("Lançada uma exceção", re);
    		//return null;
    	}
    	
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
        PolicyError error = session.getProvider(PasswordPolicyManagerProvider.class).validate(realm, user, password);
        logger.info("isValid: input -> " + cred.getValue() + " user password -> " + password);
        logger.info("isValid: error: " + error);
        return password != null && password.equals(cred.getValue()) && error == null;
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
	        cidadao.setPrimeiroLogin(false);
	        
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
    	this.identityStore = restStore;
    }

}
