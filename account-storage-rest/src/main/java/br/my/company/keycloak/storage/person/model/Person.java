
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

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private boolean isPrimeiroLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public boolean isPrimeiroLogin() {
    	return this.isPrimeiroLogin;
    }
    
    public void setPrimeiroLogin(boolean isPrimeiroLogin) {
    	this.isPrimeiroLogin = isPrimeiroLogin;
    }
    
    @Override
    public String toString() {
    	return "{"
    			+ "\"id\":" + getId()
    			+ "\"senha\":" + getSenha()
    			+ "\"nome\":" + getNome()
    			+ "\"email\":" + getEmail()
    			+ "\"telefone\":" + getTelefone()
    			+ "\"isPrimeiroLogin\":" + isPrimeiroLogin()
    			+ "}";
    }
}
