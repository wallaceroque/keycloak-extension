#!/bin/bash

# Build
mvn clean package -Pfreemarker

# Removendo o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="module remove --name=br.my.company.keycloak.custom-themes" --connect

# Implantando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="module add --name=br.my.company.keycloak.custom-themes --resources=workspace/custom-themes/target/custom-themes.jar" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"