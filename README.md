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

