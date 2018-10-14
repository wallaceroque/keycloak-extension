## Build provider
FROM maven:3.5-jdk-8 AS provider-rest

ENV MAVEN_CONFIG /var/maven/.m2

WORKDIR /project

COPY ./account-storage-rest .

RUN mvn -Duser.home=/var/maven clean package

## Build authenticator
FROM maven:3.5-jdk-8 AS custom-authenticator

ENV MAVEN_CONFIG /var/maven/.m2

WORKDIR /project

COPY --from=provider-rest /var/maven/.m2 /var/maven/.m2

COPY ./custom-authenticator .

RUN mvn -Duser.home=/var/maven clean package

## Build themes

FROM maven:3.5-jdk-8 AS custom-themes

ENV MAVEN_CONFIG /var/maven/.m2

WORKDIR /project

COPY --from=custom-authenticator /var/maven/.m2 /var/maven/.m2

COPY ./custom-themes .

RUN mvn -Duser.home=/var/maven clean package -Pfreemarker

## Keycloak

FROM jboss/keycloak-ha-postgres:3.4.3.Final

COPY --from=provider-rest /project/target ./account-storage-rest
COPY --from=custom-authenticator /project/target ./custom-authenticator
COPY --from=custom-themes /project/target ./custom-themes

ENV JBOSS_USER jboss
ENV JBOSS_PASSWORD jboss00#]

RUN mkdir /opt/jboss/keycloak/realms

COPY ./realms/person-v0.0.1.json /opt/jboss/keycloak/realms/person.json

COPY ./cli/*.cli /opt/jboss/keycloak/cli/

RUN cd /opt/jboss/keycloak && bin/jboss-cli.sh --file=cli/batch.cli && rm -rf /opt/jboss/keycloak/standalone/configuration/standalone_xml_history

RUN /opt/jboss/keycloak/bin/add-user.sh ${JBOSS_USER} ${JBOSS_PASSWORD} --silent

CMD ["-b", "0.0.0.0","-bmanagement","0.0.0.0", "--server-config", "standalone-ha.xml", "-Dkeycloak.import=/opt/jboss/keycloak/realms/person.json"]