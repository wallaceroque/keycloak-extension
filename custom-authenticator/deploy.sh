#!/bin/bash

sudo docker exec -it keycloak-server /opt/eap/bin/jboss-cli.sh -c "deploy --force /opt/eap/workspace/siac-authenticator/target/siac-authenticator.jar"

