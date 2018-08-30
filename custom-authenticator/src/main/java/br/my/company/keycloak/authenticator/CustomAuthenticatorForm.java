package br.my.company.keycloak.authenticator;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ModelDuplicateException;
import org.keycloak.models.ModelException;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.services.ServicesLogger;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.messages.Messages;

/**
 * Custom authenticator to custom login
 * 
 */
public class CustomAuthenticatorForm extends AbstractUsernameFormAuthenticator implements Authenticator {
	
	private static final Logger logger = Logger.getLogger(CustomAuthenticatorForm.class);

	@Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey("cancel")) {
            context.cancelLogin();
            return;
        }
        if (!validateForm(context, formData)) {
            return;
        }
        context.success();
    }
	
	protected boolean validateForm(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        return validateUserAndPassword(context, formData);
    }
	
	public boolean validateUserAndPassword(AuthenticationFlowContext context,
			MultivaluedMap<String, String> inputData) {
		String username = inputData.getFirst(AuthenticationManager.FORM_USERNAME);
		if (username == null) {
			context.getEvent().error(Errors.USER_NOT_FOUND);
			Response challengeResponse = invalidUser(context);
			context.failureChallenge(AuthenticationFlowError.INVALID_USER, challengeResponse);
			return false;
		}

		// remove leading and trailing whitespace
		username = username.trim();

		context.getEvent().detail(Details.USERNAME, username);
		context.getAuthenticationSession().setAuthNote(AbstractUsernameFormAuthenticator.ATTEMPTED_USERNAME, username);

		UserModel user = null;
		try {
			user = KeycloakModelUtils.findUserByNameOrEmail(context.getSession(), context.getRealm(), username);
		} catch (ModelDuplicateException mde) {
			ServicesLogger.LOGGER.modelDuplicateException(mde);

			// Could happen during federation import
			if (mde.getDuplicateFieldName() != null && mde.getDuplicateFieldName().equals(UserModel.EMAIL)) {
				setDuplicateUserChallenge(context, Errors.EMAIL_IN_USE, Messages.EMAIL_EXISTS,
						AuthenticationFlowError.INVALID_USER);
			} else {
				setDuplicateUserChallenge(context, Errors.USERNAME_IN_USE, Messages.USERNAME_EXISTS,
						AuthenticationFlowError.INVALID_USER);
			}

			return false;
			/*
			 * Customização para capturar eventuais exceções geradas a partir da consulta de
			 * um storage provider externo (user federated provider)
			 */
		} catch (ModelException me) {
			ServicesLogger.LOGGER.failedAuthentication(me.getCause());
			
			logger.info("Exception captured " + me.getCause().getMessage());
			
			setUserStorageProviderError(context, user, me.getCause().getMessage(), me.getMessage(),
					AuthenticationFlowError.IDENTITY_PROVIDER_ERROR);

			return false;
		}
		/* Fim da customização */

		if (invalidUser(context, user)) {
			return false;
		}

		if (!validatePassword(context, user, inputData)) {
			return false;
		}

		if (!enabledUser(context, user)) {
			return false;
		}

		String rememberMe = inputData.getFirst("rememberMe");
		boolean remember = rememberMe != null && rememberMe.equalsIgnoreCase("on");
		if (remember) {
			context.getAuthenticationSession().setAuthNote(Details.REMEMBER_ME, "true");
			context.getEvent().detail(Details.REMEMBER_ME, "true");
		} else {
			context.getAuthenticationSession().removeAuthNote(Details.REMEMBER_ME);
		}
		context.setUser(user);
		return true;
	}

	public Response setUserStorageProviderError(AuthenticationFlowContext context, UserModel user, String error,
			String message, AuthenticationFlowError flowError) {
		dummyHash(context);
		context.getEvent().error(error);
		Response challengeResponse = context.form().setError(message).createLogin();
		// context.failureChallenge(AuthenticationFlowError.INVALID_USER,
		// challengeResponse);
		context.forceChallenge(challengeResponse);

		return challengeResponse;

	}
	
	protected Response challenge(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        LoginFormsProvider forms = context.form();

        if (formData.size() > 0) forms.setFormData(formData);

        return forms.createLogin();
    }

	@Override
    public void authenticate(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl<>();
        String loginHint = context.getAuthenticationSession().getClientNote(OIDCLoginProtocol.LOGIN_HINT_PARAM);

        String rememberMeUsername = AuthenticationManager.getRememberMeUsername(context.getRealm(), context.getHttpRequest().getHttpHeaders());

        if (loginHint != null || rememberMeUsername != null) {
            if (loginHint != null) {
                formData.add(AuthenticationManager.FORM_USERNAME, loginHint);
            } else {
                formData.add(AuthenticationManager.FORM_USERNAME, rememberMeUsername);
                formData.add("rememberMe", "on");
            }
        }
        Response challengeResponse = challenge(context, formData);
        context.challenge(challengeResponse);
    }

	@Override
	public boolean requiresUser() {
		return false;
	}

	@Override
	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		return true;
	}

	@Override
	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

	}
	
	@Override
    public void close() {

    }

}
