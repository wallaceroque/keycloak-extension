
package br.my.company.keycloak.storage.person.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.my.company.keycloak.storage.rest.model.Entity;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "person", propOrder = { "id" })
@JsonIgnoreProperties(ignoreUnknown=true)
public class Person implements Entity {
    
    private Long id;

    private String name;
    private String email;
    private String password;
    private String telephone;
    private boolean isEnabled;
    private boolean isMustChangePassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isMustChangePassword() {
    	return this.isMustChangePassword;
    }
    
    @Override
    public String toString() {
    	return "{"
    			+ "\"id\":" + getId()
    			+ "\"password\":" + getPassword()
    			+ "\"name\":" + getName()
    			+ "\"email\":" + getEmail()
    			+ "\"telephone\":" + getTelephone()
    			+ "\"isEnabled\":" + isEnabled()
    			+ "\"isMustChangePassword\":" + isMustChangePassword()
    			+ "}";
    }
}
