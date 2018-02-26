/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.gov.dataprev.keycloak.storage.cidadao;

import javax.naming.InitialContext;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CidadaoStorageProviderFactory implements UserStorageProviderFactory<CidadaoStorageProvider> {
    private static final Logger logger = Logger.getLogger(CidadaoStorageProviderFactory.class);
    
    private CidadaoIdentityStoreRegistry restStoreRegistry;

    @Override
    public CidadaoStorageProvider create(KeycloakSession session, ComponentModel model) {
    	try {
            InitialContext ctx = new InitialContext();
            CidadaoStorageProvider provider = (CidadaoStorageProvider)ctx.lookup("java:global/user-storage-rest/" + CidadaoStorageProvider.class.getSimpleName());
            provider.setModel(model);
            provider.setSession(session);
            provider.setIdentityStore(this.restStoreRegistry.getRestStore(session, model));
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getId() {
        return "Cidadão Storage Provider";
    }

    @Override
    public String getHelpText() {
        return "Provider de usuários integrados a base de dados do SIAC";
    }
    
    @Override
    public void init(Config.Scope config) {
        this.restStoreRegistry = new CidadaoIdentityStoreRegistry();
    }

    @Override
    public void close() {
        logger.info("<<<<<< Closing factory");
        this.restStoreRegistry = null;

    }
}