# Person Service

**person-service** is a NodeJS project to simulate a API REST of Person domain.

The [Loopback 3](https://loopback.io/) offers a rich API to the domain declared in JSON files:

```json
{
  "name": "Person",
  "plural": "persons",
  "forceId": false,
  "remoting" : {
    "normalizeHttpPath" : true
  },
  "properties": {
    "id": {
      "type": "number",
      "required": true,
      "id": true
    },
    "password": {
      "type": "string",
      "required": true
    },
    "name": {
      "type": "string"
    },
    "email": {
      "type": "string"
     },
    "telephone": {
      "type": "string"
    }
  }
}
```

The API can be consulting through a Swagger embedded into application in address  **http://localhost:3000/explorer**.

![API Person](docs/images/api-person-node.png)

## Starting project

```bash
# Installation of NodeJS modules
npm install

# Starting project
npm start
```

## Data persistence

Data are persisted in file: `<root_of_project>/data/db.json`.

To persistence in databases look [Loopback's documentation](http://loopback.io/doc/en/lb3/Defining-data-sources.html)
