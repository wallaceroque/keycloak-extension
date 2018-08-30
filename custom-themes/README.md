# Temas customizados do Keycloak

Esse projeto foi criado para testes de customização dos temas do Keycloak. O tema utilizado no projeto custom-themes é o mesmo utilizado na distribuição da RedHat do Keycloak: RH-SSO. Como o projeto é para estudos e visa ter um projeto básico para customização de temas, ainda não foi estilizado.

## Construindo e implantando o projeto de customização do tema

Na raiz do projeto **custom-themes**, executar:

```bash
mvn clean package -Pfreemarker
```

Após a construção é necessário implantá-lo como módulo no JBoss:

```bash
# Implantando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="module add --name=br.my.company.keycloak.custom-themes --resources=workspace/custom-themes/target/custom-themes.jar" --connect

# Habilitando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="/subsystem=keycloak-server/theme=defaults:list-add(name=modules,value="br.my.company.keycloak.custom-themes")" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"

```

Caso seja necessário atualizar o módulo previamente implantado, remova-o, reinstale-o e reinicie o servidor:

```bash
# Removendo o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="module remove --name=br.my.company.keycloak.custom-themes" --connect

# Implantando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="module add --name=br.my.company.keycloak.custom-themes --resources=workspace/custom-themes/target/custom-themes.jar" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"
```

## Observação

Para o modo standalone:

** = /subsystem=keycloak-server

Em modo domain, algo semelhante:

** = /profile=auth-server-clustered/subsystem=keycloak-server