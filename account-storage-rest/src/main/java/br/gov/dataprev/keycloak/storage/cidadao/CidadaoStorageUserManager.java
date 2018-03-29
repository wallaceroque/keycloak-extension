package br.gov.dataprev.keycloak.storage.cidadao;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.models.UserModel;

import br.gov.dataprev.keycloak.storage.cidadao.model.Cidadao;

public class CidadaoStorageUserManager {
	
	private final Map<String, ManagedUserEntry> managedUsers = new HashMap<>();
    private final CidadaoStorageProvider provider;

    public CidadaoStorageUserManager(CidadaoStorageProvider provider) {
        this.provider = provider;
    }
    
    public UserModel getManagedProxiedUser(String userId) {
        ManagedUserEntry entry = managedUsers.get(userId);
        return entry==null ? null : entry.getManagedProxiedUser();
    }

    public Cidadao getManagedCidadaoUser(String userId) {
        ManagedUserEntry entry = managedUsers.get(userId);
        return entry==null ? null : entry.getCidadaoUser();
    }
    
    public void setManagedProxiedUser(UserModel proxiedUser, Cidadao cidadao) {
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
        private final Cidadao cidadaoUser;
        //private final LDAPTransaction ldapTransaction;

        public ManagedUserEntry(UserModel managedProxiedUser, Cidadao cidadaoUser) {
            this.managedProxiedUser = managedProxiedUser;
            this.cidadaoUser = cidadaoUser;
            //this.ldapTransaction = ldapTransaction;
        }

        public UserModel getManagedProxiedUser() {
            return managedProxiedUser;
        }

        public Cidadao getCidadaoUser() {
            return cidadaoUser;
        }

        /*public LDAPTransaction getLdapTransaction() {
            return ldapTransaction;
        }*/
    }

}
