package br.my.company.keycloak.storage.person;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.models.UserModel;

import br.my.company.keycloak.storage.person.model.Person;

public class CidadaoStorageUserManager {
	
	private final Map<String, ManagedUserEntry> managedUsers = new HashMap<>();
    private final PersonStorageProvider provider;

    public CidadaoStorageUserManager(PersonStorageProvider provider) {
        this.provider = provider;
    }
    
    public UserModel getManagedProxiedUser(String userId) {
        ManagedUserEntry entry = managedUsers.get(userId);
        return entry==null ? null : entry.getManagedProxiedUser();
    }

    public Person getManagedCidadaoUser(String userId) {
        ManagedUserEntry entry = managedUsers.get(userId);
        return entry==null ? null : entry.getPersonUser();
    }
    
    public void setManagedProxiedUser(UserModel proxiedUser, Person cidadao) {
        String userId = proxiedUser.getId();
        ManagedUserEntry entry = managedUsers.get(userId);
        if (entry != null) {
            throw new IllegalStateException("Don't expect to have entry for user " + userId);
        }

        //LDAPTransaction ldapTransaction = new LDAPTransaction(provider, ldapObject);
        ManagedUserEntry newEntry = new ManagedUserEntry(proxiedUser, cidadao);
        managedUsers.put(userId, newEntry);
    }
    
    private static class ManagedUserEntry {

        private final UserModel managedProxiedUser;
        private final Person person;
        //private final LDAPTransaction ldapTransaction;

        public ManagedUserEntry(UserModel managedProxiedUser, Person user) {
            this.managedProxiedUser = managedProxiedUser;
            this.person = user;
            //this.ldapTransaction = ldapTransaction;
        }

        public UserModel getManagedProxiedUser() {
            return managedProxiedUser;
        }

        public Person getPersonUser() {
            return person;
        }

        /*public LDAPTransaction getLdapTransaction() {
            return ldapTransaction;
        }*/
    }

}
