# Custom Keycloak Services Project

This is a project to extend the keycloak services module. Customizing this project can bring unexpected behaviours. Be careful!!

Obs.: If you have a RedHat's subscription for the RH-SSO, product based on the Keycloak, consult and valid the RedHat for your customizations 

## Building and deploy this project

In project's root of **custom-keycloak-services**, to build, run:

```bash
# Build 
mvn clean package
```
Since the workspace is mapped to the volume docker, run:

```bash

# Copy jar to keycloak services module
docker exec -it keycloak-server cp workspace/custom-keycloak-services/target/custom-keycloak-services-3.4.3.Final-0.0.1-SNAPSHOT.jar keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/
```

Edit module.xml store in `keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/` 

```bash
docker exec -it keycloak-server vi keycloak/modules/system/layers/keycloak/org/keycloak/keycloak-services/main/module.xml
```

```xml
...
<module xmlns="urn:jboss:module:1.3" name="org.keycloak.keycloak-services">
    <properties>
        <property name="jboss.api" value="private"/>
    </properties>

    <resources>
        <resource-root path="custom-keycloak-services-3.4.3.Final-0.0.1-SNAPSHOT.jar"/>
    </resources>
...
```

Restart server:

```bash
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"
```
