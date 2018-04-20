package br.my.company.keycloak.storage.person.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ModelException;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import br.my.company.keycloak.storage.rest.RESTIdentityStore;

/**
 * This class is responsible to match keycloak's user and business's object
 * @author <a href="mailto:wallacerock@gmail.com">Wallace Roque</a>
 * @version $Revision: 1 $
 */
public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    private static final Logger logger = Logger.getLogger(UserAdapter.class);
    public static String FULL_NAME_ATTRIBUTE = "name";
    public static String TELEPHONE_ATTRIBUTE = "telephone";
    
    protected Person entity;
    protected String keycloakId;
    protected RESTIdentityStore<Person> identityStore;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, RESTIdentityStore<Person> identityStore, Person entity) {
        super(session, realm, model);
        this.entity = entity;
        logger.info("UserAdapter.entity: " + entity.toString());
        this.keycloakId = StorageId.keycloakId(model, String.valueOf(this.entity.getId()));
        this.identityStore = identityStore;
    }

    public String getPassword() {
    	return this.entity.getPassword();
    }

    public void setPassword(String password) {
        this.entity.setPassword(password);
    }

    @Override
    public String getUsername() {
        return this.entity.getId().toString();
    }

    @Override
    public void setUsername(String username) {
        throw new ReadOnlyException("Username can not be changed!");
    }

    @Override
    public void setEmail(String email) {
    	logger.info("UserAdapter.setEmail: " + email);
        this.entity.setEmail(email);
        try {
        	this.identityStore.update(entity);
        } catch (Exception e) {
    		throw new ModelException(e.getMessage(), EMAIL_ATTRIBUTE);
    	}
        
        //super.setEmail(email);
    }

    @Override
    public String getEmail() {
        return this.entity.getEmail();
    }

    @Override
    public String getId() {
        return this.keycloakId;
    }
    
//    @Override
//    public boolean isEnabled() {
//    	return this.entity.isEnabled();
        /*String val = getFirstAttribute(ENABLED_ATTRIBUTE);
        if (val == null) return true;
        else return Boolean.valueOf(val);
        */
//    }
    
//    @Override
//    public void setEnabled(boolean enabled) {
//    	this.entity.setEnabled(enabled);
//    	try {
//    		this.identityStore.update(entity);
//    		//setSingleAttribute(ENABLED_ATTRIBUTE, Boolean.toString(enabled));    		
//    	} catch (Exception e) {
//    		//setSingleAttribute(ENABLED_ATTRIBUTE, getFirstAttribute(ENABLED_ATTRIBUTE));
//    		throw new ModelException(e.getMessage(), ENABLED_ATTRIBUTE);
//    	}
//    }
    
    @Override
    public String getFirstName() {
    	return this.entity.getName();
    }
    
    @Override
    public String getLastName() {
    	return this.entity.getName();
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (name.equals(TELEPHONE_ATTRIBUTE)) {
            this.entity.setTelephone(value);
        } 
        if (name.equals(FULL_NAME_ATTRIBUTE)) {
            throw new ReadOnlyException("Full name can not be changed!");
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals(TELEPHONE_ATTRIBUTE)) {
            this.entity.setTelephone(null);
        } else if (name.equals(FULL_NAME_ATTRIBUTE)) {
        	throw new ReadOnlyException("Full name can not be changed!");
        } else {
            super.removeAttribute(name);
        }
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (name.equals(TELEPHONE_ATTRIBUTE)) {
            this.entity.setTelephone(values.get(0));
        } 
        if (name.equals(FULL_NAME_ATTRIBUTE)) {
        	throw new ReadOnlyException("Full name can not be changed!");
        } else {
            super.setAttribute(name, values);
        }
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals(TELEPHONE_ATTRIBUTE)) {
            return this.entity.getTelephone();
        } else if (name.equals(FULL_NAME_ATTRIBUTE)) {
        	return this.entity.getName();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add(TELEPHONE_ATTRIBUTE, this.entity.getTelephone());
        all.add(FULL_NAME_ATTRIBUTE, this.entity.getName());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals(TELEPHONE_ATTRIBUTE)) {
            List<String> phone = new LinkedList<>();
            phone.add(this.entity.getTelephone());
            return phone;
        } else if (name.equals(FULL_NAME_ATTRIBUTE)) {
        	List<String> nome = new LinkedList<>();
            nome.add(this.entity.getName());
            return nome;
        } else {
            return super.getAttribute(name);
        }
    }
}
