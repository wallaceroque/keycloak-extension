package br.my.company.keycloak.storage.person.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import br.my.company.keycloak.storage.rest.RESTIdentityStore;

/**
 * TODO: Deve ser verificado se o Keycloak pode alterar o cpf e outros atributos do usuário ao utilizar this.entity.setCpf. 
 * Caso seja possível esse método deve lançar a exceção ReadOnlyException. Três situações devem ser verificadas:
 *    - Ao importar o usuário a primeira vez da base da Keycloak
 *    - Ao criar um usuário pela tela de gerenciamento de usuários
 *    - Na tela de gestão de contas do usuário
 */

/**
 * Responsável por fazer o bind entre o objeto usuário do Keycloak e o objeto de domínio do negócio
 * @author <a href="mailto:wallacerock@gmail.com">Wallace Roque</a>
 * @version $Revision: 1 $
 */
public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    private static final Logger logger = Logger.getLogger(UserAdapter.class);
    protected Person entity;
    protected String keycloakId;
    protected RESTIdentityStore<Person> identityStore;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, RESTIdentityStore<Person> identityStore, Person entity) {
        super(session, realm, model);
        this.entity = entity;
        logger.info("UserAdapter: entity: " + entity.toString());
        this.keycloakId = StorageId.keycloakId(model, String.valueOf(this.entity.getId()));
        this.identityStore = identityStore;
    }

    public String getPassword() {
    	return this.entity.getSenha();
    }

    public void setPassword(String password) {
        this.entity.setSenha(password);
    }

    @Override
    public String getUsername() {
        return this.entity.getId().toString();
    }

    @Override
    public void setUsername(String username) {
        throw new ReadOnlyException("O CPF não pode ser alterado.");
    }

    @Override
    public void setEmail(String email) {
    	logger.info("setEmail: " + email);
        this.entity.setEmail(email);
        // TODO Tratar exceção e realizar o rollback da alteração
        this.identityStore.update(entity);
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

    @Override
    public void setSingleAttribute(String name, String value) {
        if (name.equals("telefone")) {
            this.entity.setTelefone(value);
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals("telefone")) {
            this.entity.setTelefone(null);
        } else if (name.equals("nome")) {
        	this.entity.setNome(null);
        } else {
            super.removeAttribute(name);
        }
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (name.equals("telefone")) {
            this.entity.setTelefone(values.get(0));
        } else {
            super.setAttribute(name, values);
        }
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals("telefone")) {
            return this.entity.getTelefone();
        } else if (name.equals("nome" )) {
        	return this.entity.getNome();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("telefone", this.entity.getTelefone());
        all.add("nome", this.entity.getNome());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals("telefone")) {
            List<String> phone = new LinkedList<>();
            phone.add(this.entity.getTelefone());
            return phone;
        } else if (name.equals("nome")) {
        	List<String> nome = new LinkedList<>();
            nome.add(this.entity.getNome());
            return nome;
        } else {
            return super.getAttribute(name);
        }
    }
}
