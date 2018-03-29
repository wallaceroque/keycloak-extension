# Pessoa Service

**pessoa-service** é um projeto em NodeJS para simular a API REST do domínio Pessoa.

O [Loopback 3](https://loopback.io/) oferece uma API rica de acordo com o domínio declarado em arquivos JSON:

```json
{
  "name": "Pessoa",
  "plural": "pessoas",
  "forceId": false,
  "remoting" : {
    "normalizeHttpPath" : true
  },
  "properties": {
    "cpf": {
      "type": "number",
      "required": true,
      "id": true
    },
    "senha": {
      "type": "string",
      "required": true
    },
    "nome": {
      "type": "string"
    },
    "email": {
      "type": "string"
     },
    "telefone": {
      "type": "string"
    }
  }
}
```

A API criada pode ser consultada por meio do Swagger embarcado na aplicação no endereço **http://localhost:3000/explorer**.

![API Cidadão BR](docs/images/api-siac-node.png)

## Iniciando o projeto

```bash
# Instalação dos módulos NodeJS
npm install

# Iniciando projeto
npm start
```

## Persistência de dados

Os dados são persistidos em arquivo: `<raiz_do_projeto>/data/db.json`.

Para persistência em outros bancos de dados vide [documentação do Loopback](http://loopback.io/doc/en/lb3/Defining-data-sources.html)
