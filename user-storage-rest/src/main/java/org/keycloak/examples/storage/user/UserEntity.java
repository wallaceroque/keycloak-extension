
package org.keycloak.examples.storage.user;


public class UserEntity {
    
    private String cpf;

    private String nome;
    private String email;
    private String senha;
    private String telefone;

    public String getId() {
        return cpf;
    }

    public void setId(String id) {
        this.cpf = id;
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
}
