FROM maven:3.5-jdk-8 AS provider-rest

WORKDIR /project

COPY ./account-storage-rest .

RUN mvn clean package


FROM maven:3.5-jdk-8 AS custom-authenticator

WORKDIR /project

COPY ./custom-authenticator .

RUN mvn clean package


FROM jboss/keycloak-ha-postgres:3.4.3.Final

COPY --from=provider-rest /project/target ./account-storage-rest
COPY --from=custom-authenticator /project/target ./custom-authenticator

ENV JBOSS_USER jboss
ENV JBOSS_PASSWORD jboss00#

COPY ./cli/deploy*.cli /opt/jboss/keycloak/cli/

RUN cd /opt/jboss/keycloak && bin/jboss-cli.sh --file=cli/deploy-account-storage-rest.cli && rm -rf /opt/jboss/keycloak/standalone/configuration/standalone_xml_history

RUN cd /opt/jboss/keycloak && bin/jboss-cli.sh --file=cli/deploy-custom-authenticator.cli && rm -rf /opt/jboss/keycloak/standalone/configuration/standalone_xml_history

RUN /opt/jboss/keycloak/bin/add-user.sh ${JBOSS_USER} ${JBOSS_PASSWORD} --silent

CMD ["-b", "0.0.0.0","-bmanagement","0.0.0.0", "--server-config", "standalone-ha.xml"]