FROM jboss/keycloak:3.4.3.Final

ENV JBOSS_USER jboss
ENV JBOSS_PASSWORD jboss00#

RUN /opt/jboss/keycloak/bin/add-user.sh ${JBOSS_USER} ${JBOSS_PASSWORD} --silent

CMD ["-b", "0.0.0.0","-bmanagement","0.0.0.0"]