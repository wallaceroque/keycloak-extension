#!/bin/bash

# Build
mvn clean package -Pfreemarker

# Implantando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="module add --name=br.my.company.keycloak.custom-themes --resources=workspace/custom-themes/target/custom-themes.jar" --connect

# Habilitando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="/subsystem=keycloak-server/theme=defaults:list-add(name=modules,value="br.my.company.keycloak.custom-themes")" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"