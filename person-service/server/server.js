'use strict';

var loopback = require('loopback');
var boot = require('loopback-boot');

var app = module.exports = loopback();

app.start = function() {
  // start the web server
  return app.listen(function() {
    app.emit('started');
    var baseUrl = app.get('url').replace(/\/$/, '');
    console.log('Web server listening at: %s', baseUrl);
    if (app.get('loopback-component-explorer')) {
      var explorerPath = app.get('loopback-component-explorer').mountPath;
      console.log('Browse your REST API at %s%s', baseUrl, explorerPath);
    }
  });
};

// Bootstrap the application, configure models, datasources and middleware.
// Sub-apps like REST API are mounted via boot scripts.
boot(app, __dirname, function(err) {
  if (err) throw err;

  // start the server if `$ node server.js`
  if (require.main === module)
    app.start();
});

/*
var session = require('express-session');
var Keycloak = require('keycloak-connect');
var memoryStore = new session.MemoryStore();

app.use(session({
  secret: 'xxx',
  resave: false,
  saveUninitialized: true,
  store: memoryStore,
}));

let kcConfig = {
  clientId: 'person-service',
  bearerOnly: true,
  serverUrl: 'http://localhost:8080/auth',
  realm: 'microservices',
  realmPublicKey: 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtMuOJ0yagTwDnIrSdRByKkAzTimCONjWEe91V9mfzfw5uPBCnPCYVQc4294zbzq8ouKrydM7vToyXirJySqQj9HtfWQmMAsg7uZCkOiGZWfGEOX+9APpkGif1pbl2xu/EdwR9kbIp0XZAxzu4+v6fycpzLkQGB3E/hFO0julGQ4BtSgupLSscHJ6/drMqww5NvIX8V76jY6Wdo5098T7z/O1fAG95w3lIWJDqT2DNzqvFm5wg6zSVAoFfNHrYMaF6z7Fhtnzbp775uELxrUQ7ybAXepbTby6uVjSnDU7k4JznHU7TNinBctqf+5f1iaGe+EZDnm6mp6dE+DLc4OWdQIDAQAB',
};

var keycloak = new Keycloak({store: memoryStore}, kcConfig);

app.use(keycloak.middleware());

app.get('/api/*', keycloak.protect(), function(req, resp, next) {
  next();
});
*/
