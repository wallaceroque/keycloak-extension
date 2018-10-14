
package br.my.company.keycloak.storage.person;

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
import org.keycloak.events.Errors;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ModelException;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserModel.RequiredAction;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.credential.PasswordUserCredentialModel;
import org.keycloak.policy.PasswordPolicyManagerProvider;
import org.keycloak.policy.PolicyError;
import org.keycloak.services.messages.Messages;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import br.my.company.keycloak.storage.person.model.Person;
import br.my.company.keycloak.storage.person.model.UserAdapter;
import br.my.company.keycloak.storage.rest.RESTIdentityStoreException;

/**
 * @author <a href="mailto:wallacerock@gmail.com">Wallace Roque</a>
 * @version $Revision: 1 $
 */
public class PersonStorageProvider implements 
		UserStorageProvider,
		UserLookupProvider,
		CredentialInputValidator,
		CredentialInputUpdater
		//UserQueryProvider,
		//CredentialAuthentication,
		//ImportedUserValidation
        // OnUserCache
{
    private static final Logger logger = Logger.getLogger(PersonStorageProvider.class);
    public static final String PASSWORD_CACHE_KEY = UserAdapter.class.getName() + ".password";

    protected ComponentModel model;
    protected KeycloakSession session;
    
    protected PersonStorageProviderFactory factory;
    protected PersonIdentityStore identityStore;
    protected PersonStorageUserManager userManager;
    
    protected final Set<String> supportedCredentialTypes = new HashSet<>();
    
    public PersonStorageProvider(PersonStorageProviderFactory factory, KeycloakSession session, ComponentModel model, PersonIdentityStore identityStore) {
    	logger.debug("provider: constructor");
        this.factory = factory;
        this.session = session;
        this.model = model;
        this.identityStore = identityStore;
        this.userManager = new PersonStorageUserManager(this);

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
	public UserModel getUserById(String keycloakId, RealmModel realm) throws ModelException {
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
	public UserModel getUserByUsername(String id, RealmModel realm) throws ModelException {
		//////// DEBUG-START
    	logger.info("PersonStorageProvider.getUserByUsername: id: " + id);
//    	logger.info("PersonStorageProvider.getUserByUsername: count local storage: " + session.userLocalStorage().getUsersCount(realm));
    	logger.info("PersonStorageProvider.getUserByUsername: count federated storage: " + session.userFederatedStorage().getStoredUsersCount(realm));
//    	Set<FederatedIdentityModel> models = session.userFederatedStorage().getFederatedIdentities(new StorageId(id).getId(), realm);
//    	logger.info("PersonStorageProvider.getUserByUsername: count federated identity model stored: " + models.size());
    	
//		List<UserModel> users = session.userLocalStorage().getUsers(realm);
//        users.forEach(user -> 
//        	logger.info("PersonStorageProvider.getUserByUsername: user from local storage: id: " + 
//        			user.getId() + " username: " + user.getUsername()));
        
        List<String> federatedStoredUsers = session.userFederatedStorage().getStoredUsers(realm, 0, 5);
        
        federatedStoredUsers.forEach(user -> {
        	logger.info("PersonStorageProvider.getUserByUsername: user from federated storage: " + user);
            session.userFederatedStorage()
        		.getRequiredActions(realm, user)
        		.forEach(requiredAction -> 
        			logger.info("PersonStorageProvider.getUserByUsername: required action from federated storage: " + requiredAction));
            });

    	//////// DEBUG-END
        ///////////////////////////////////////////////////////////
        Person person = null;
    	try{            
    		person = identityStore.searchById(Long.valueOf(id));
    		
    		if (person == null) return null;
    		
    		if (!person.isEnabled()) {
    			RuntimeException wrapper = new RuntimeException(Errors.INVALID_USER_CREDENTIALS, new IllegalStateException("User not complete your registration"));
    			throw new ModelException("userDidNotCompleteRegistration", wrapper);
    		}
    		
    	} catch(NumberFormatException nfe) {
    		logger.error("PersonStorageProvider.getUserByUsername: Format's id is invalid! ID: " + id);
    		RuntimeException wrapper = new RuntimeException(Errors.INVALID_FORM, nfe);
    		throw new ModelException(Messages.INVALID_USER, wrapper);
    		
    	}  catch(RESTIdentityStoreException ris) {
    		logger.error(ris.getCause());
    		throw new ModelException(ris.getMessage(), ris.getCause());
    	}
    	            
        UserAdapter userAdapter = new UserAdapter(session, realm, model, this.identityStore, person);
        
        if (userAdapter.isEnabled()) {
        	if (person.isMustChangePassword()) {
        		this.forceUpdatePasswordInNextLogin(realm, userAdapter);
        	}
        } else {
        	session.authenticationSessions().close();
        }
        
        //userManager.setManagedProxiedUser(adapter, person);

        return userAdapter;
    	
	}
    
    @Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
    	logger.info("PersonStorageProvider.getUserByEmail: email: " + email);
    	Person person = null;
    	
    	try {
    		person = identityStore.searchByEmail(email);
    		
    		if (person == null) return null;
    		
    	} catch(RESTIdentityStoreException ris) {
    		logger.error("Throw ModelException!", ris);
    		throw new ModelException(ris.getMessage(), ris.getCause());
    	}
         
        UserAdapter adapter = new UserAdapter(session, realm, model, this.identityStore, person);

        return adapter;
	}
    
    // CredentialInputValidator
    
    @Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
    	return getSupportedCredentialTypes().contains(credentialType);
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
		logger.info("PersonStorageProvider.isValid: UserModel.getId() -> " + user.getId());
		
		if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
//        UserAdapter userAdapter = (UserAdapter)getUserByUsername(StorageId.externalId(user.getId()), realm);
		Person person = identityStore.searchById(Long.valueOf((StorageId.externalId(user.getId()))));
		
		if (person == null) return false;
		
		if (!person.isEnabled()) {
			//TODO Possível pontos de problema
			//RuntimeException wrapper = new RuntimeException(Errors.INVALID_USER_CREDENTIALS, new IllegalStateException("User not complete your registration"));
			IllegalStateException illegalStateException = new IllegalStateException(
					"User did not complete registration");
			throw new ModelException("userDidNotCompleteRegistration", illegalStateException);
		}
		
        //String password = getPassword(adapter);
		String password = person.getPassword();
        UserCredentialModel cred = (UserCredentialModel)input;
        logger.info("PersonStorageProvider.isValid: input -> " + cred.getValue() + " user password -> " + password);
        PolicyError error = session.getProvider(PasswordPolicyManagerProvider.class).validate(realm, user, password);
        if (error != null) {
        	logger.info("PersonStorageProvider.isValid: error: " + error);
        	this.forceUpdatePasswordInNextLogin(realm, user);
        }
        
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
		return Collections.emptySet();
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
        	Person person = identityStore.searchById(Long.valueOf((StorageId.externalId(user.getId()))));
	        person.setPassword(password);
	        
	        identityStore.update(person);
	        
	        // Remove required action UPDATE_PASSWORD when user is forced to change
	        if (user.getRequiredActions().contains(RequiredAction.UPDATE_PASSWORD.toString())) {
	        	user.removeRequiredAction(RequiredAction.UPDATE_PASSWORD);
	        	logger.info("PersonStorageProvider.updateCredential: RequiredAction.UPDATE_PASSWORD removed");
	        }
	        logger.info("updateCredential: Change password with success!");
	        return true;
	    } catch (RESTIdentityStoreException ris) {
	    	logger.info("updateCredential: Change password failed ", ris);
	    	throw new ModelException(ris.getMessage(), ris.getCause());
	    }
	}
	
	private void forceUpdatePasswordInNextLogin(RealmModel realm, UserModel userAdapter) {
		Set<String> actions = userAdapter.getRequiredActions();
		
		actions.forEach(action -> logger.info("PersonStorageProvider.getUserByUsername: " + action));
		
		if (actions.contains(RequiredAction.UPDATE_PASSWORD.toString())){
			logger.info("PersonStorageProvider.forceUpdatePasswordInNextLogin: Required Action is already associated with the user.");
			logger.info("PersonStorageProvider.forceUpdatePasswordInNextLogin: Removing existing RequiredAction.UPDATE_PASSWORD");
			userAdapter.removeRequiredAction(RequiredAction.UPDATE_PASSWORD);
		}
		userAdapter.addRequiredAction(RequiredAction.UPDATE_PASSWORD);
		logger.info("PersonStorageProvider.getUserByUsername: RequiredAction.UPDATE_PASSWORD added!!");
		session.userCache().evict(realm, userAdapter);
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
    
    public void setIdentityStore(PersonIdentityStore restStore) {
    	this.identityStore = restStore;
    }

}
