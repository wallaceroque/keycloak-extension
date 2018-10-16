'use strict';

module.exports = {
  db: {
    name: 'db',
    connector: 'memory',
  },
  mongo: {
    name: 'mongo',
    connector: 'mongodb',
    hostname: process.env.DB_HOST || 'mongo',
    port: process.env.DB_PORT || 27017,
    user: process.env.DB_USER || 'person',
    password: process.env.DB_PASSWORD || 'personDB',
    database: process.env.DB || 'person',
  },
  transient: {
    name: 'transient',
    connector: 'transient',
  },
};
