# Painel de Gestão de Contas do Usuário [DRAFT]

Esse projeto foi criado para testar a criação de um resource realm do Keycloak que sirva como backend da interface do painel de gestão de contas.

A intenção é estender as funcionalidades já embarcados no Keycloak, entretanto, isso pode trazer alguns efeitos indesejados e, por isso, esse projeto foi somente iniciado e, por enquanto, não há intenção de continuá-lo.

## Construindo e implantando o projeto de customização do painel de gestão de conta do usuário

```bash
# Implantando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="module add --name=br.my.company.keycloak.account-profile-panel \n --dependencies=org.keycloak.keycloak-core,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,org.keycloak.keycloak-services,javax.ws.rs.api \n --resources=workspace/account-profile-panel/target/account-profile-panel.jar" --connect

# Habilitando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="/subsystem=keycloak-server:list-add(name=providers,value="module:br.my.company.keycloak.account-profile-panel")" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"
```

Caso seja necessário atualizar o módulo previamente implantado, remova-o e reinicie o servidor:

```bash
# Removendo o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh "module remove --name=br.my.company.keycloak.account-profile-panel" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"
```

## Observação

Para o modo standalone:

** = /subsystem=keycloak-server

Em modo domain, algo semelhante:

** = /profile=auth-server-clustered/subsystem=keycloak-server