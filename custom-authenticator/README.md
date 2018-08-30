# User Storage Provider utilizando REST

Este projeto visa a extensão de uma base federada do SIAC (Cidadão.BR) acessada via API REST.

## Configurações, build e implantação

### Configuração do ambiente

Para configurar o projeto é necessário definir o endereço da API REST do SIAC, utilizando variável de ambiente ou propriedade configurado no container JBoss EAP por exemplo `SIAC_CONNECTION_URL=http://<endereco_do_servidor>:<porta>/api`

### Build

Por ser um projeto Java (.jar), é necessário utilizar o Maven para construir e empacotar a extensão:

```bash
mvn clean package
```

O projeto possui o plugin do Wildfly que pode ser utilizado para implantação em tempo de desenvolvimento. É necessário configurá-lo no pom.xml:

```xml
<plugin>
    <groupId>org.wildfly.plugins</groupId>
    <artifactId>wildfly-maven-plugin</artifactId>
    <configuration>
        <skip>false</skip>
        <hostname><endereço_do_servidor></hostname>
        <username><usuario></username>
        <password><senha></password>
    </configuration>
</plugin>
```

```bash
# Buildar e implantar no JBoss EAP
mvn clean install wildfly:deploy
```

### Implantação

Para implantação do artefato diretamente no JBoss EAP, pode ser utilizado o `jboss-cli`:

```bash
<JBOSS_HOME>/bin/jboss-cli.sh -c "deploy <caminho_do_artefato>"
```

Caso esteja utilizando **Docker**, verifique se o seu workspace, ou o projeto, está mapeado como volume do container:

```bash
docker exec -it [nome_do_container] <JBOSS_HOME>/bin/jboss-cli.sh -c "deploy <workspace/caminho_do_artefato>"
```


