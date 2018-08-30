package br.my.company.keycloak.authenticator;

import java.util.List;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.provider.ProviderConfigProperty;

/**
 * Factory class to custom login
 *
 */
public class CustomAuthenticatorFormFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {
	
	Logger logger = Logger.getLogger(this.getClass());
    public static final String PROVIDER_ID = "custom-auth-form";
    public static final CustomAuthenticatorForm SINGLETON = new CustomAuthenticatorForm();
    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED
    };
        
	@Override
	public Authenticator create(KeycloakSession session) {
		logger.info("Creating Custom Authenticator Username Password Form");
        return SINGLETON;

	}
	
	@Override
	public void init(Scope config) {}
	
	@Override
	public void postInit(KeycloakSessionFactory factory) {}
	
	@Override
	public void close() {
		logger.info("<<<<<< Closing factory");
	}
	
	@Override
	public String getId() {
		return PROVIDER_ID;
	}
	
	@Override
	public String getDisplayType() {
		return "Username and password form to custom authenticator";
	}
	
	@Override
	public String getReferenceCategory() {
		return UserCredentialModel.PASSWORD;
	}
	
	@Override
    public boolean isConfigurable() {
        return false;
    }
    
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }
	
	
	@Override
	public String getHelpText() {
		return "Validates a username and password from login form.";
	}
		
	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return null;
	}
	
	@Override
	public boolean isUserSetupAllowed() {
		return false;
	}
}
