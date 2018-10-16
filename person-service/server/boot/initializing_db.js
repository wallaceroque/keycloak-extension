'use strict';
const async = require('async');

module.exports = async function (app) {
  const mongoDs = app.dataSources.mongo;
  // Initialize DB
  if (process.env.INITIALIZE_DB) {
    try {
      console.log('Initializing db...');
      let result = await createPersons();
      console.log(result);
    } catch (err) {
      console.log(err);
    }
  }

  async function createPersons(cb) {
    mongoDs.autoupdate('Person', (err) => {
      if (err) return cb(err);

      const Person = app.models.Person;
      Person.create([{
        id: '111',
        password: '123123',
        name: 'Zezinho das Couves',
        email: 'zezinho.couves@gmail.com',
        telephone: '(21)969696999',
      }, {
        id: '222',
        password: '321321',
        name: 'Fulano de Tal',
        email: 'fulano.tal@gmail.com',
        telephone: '(21)996969696',
      }], cb);
    });
  }
};
