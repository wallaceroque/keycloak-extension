# Keycloak Extension

Projeto para construção de ambiente mínimo para testes com o Keycloak.

O objetivo é customizar o Keycloak utilizando as SPI (Service Provider Interface) para estendê-lo.

## Tecnologias

- Docker versão 17.12.0-ce
- Docker Compose versão 1.19.0
- Keycloak versão 3.4.3.Final
- PostgreSQL versão 9.4.5
- Node 8.x
- Loopback 3

## Estrutura do repositório

- **cidadao-service** - Simples serviço de contas do usuário para integração com o Keycloak
- **dtp-themes** - Projeto para customização do tema do Keycloak
- **user-storage-rest** - Projeto para extensão e integração com a base de usuários do Keycloak utilizando REST

## Executando

Utilize o **docker-compose.yml** para iniciar o servidor do Keycloak e banco. Será realizado o build da imagem e o start dos serviços.

```bash
docker-compose up
```

O JBoss estará com a console de gerenciamento habilitado. Utilize as credenciais **jboss:jboss00#** para o login.

Executando o serviço de contas:

```bash
npm install && npm start
```

## Mapeamento do workspace para o container

Para facilitar a implantação de alguns artefatos, foi mapeado a raiz do projeto para o diretório /opt/jboss/workspace:

```yaml
volumes:
    - ./:/opt/jboss/workspace
```

## Construindo e implantando o projeto de integração

Na raiz do projeto **user-storage-rest**, executar:

```bash
mvn clean wildfly-deploy
```

## Construindo e implantando o projeto de customização do tema

Na raiz do projeto **dtp-themes**, executar:

```bash
mvn clean package -Pfreemarker
```

Após a construção é necessário implantá-lo como módulo no JBoss:

```bash
# Implantando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh "module add --name=br.gov.dataprev.keycloak.dtp-themes --resources=workspace/dtp-themes/target/dtp-themes.jar" --connect

# Habilitando o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh --command="/subsystem=keycloak-server/theme=defaults:list-add(name=modules,value="br.gov.dataprev.keycloak.dtp-themes")" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"

```

Caso seja necessário atualizar o módulo previamente implantado, remova-o e reinicie o servidor:

```bash
# Removendo o módulo
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh "module remove --name=br.gov.dataprev.keycloak.dtp-themes" --connect

# Reiniciando o servidor
docker exec -it keycloak-server keycloak/bin/jboss-cli.sh -c ":shutdown(restart=true)"
```

**Observação**:

Para o modo standalone:

** = /subsystem=keycloak-server

Em modo domain, algo semelhante:

** = /profile=auth-server-clustered/subsystem=keycloak-server
