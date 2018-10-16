'use strict';

module.exports = function(Person) {
  Person.observe('before save', (ctx, next) => {
    const person = ctx.currentInstance;
    const passwordChanged = (ctx.data && ctx.data.password) ?
      ctx.data.password : null;
    if (person && passwordChanged &&
      (person.password != passwordChanged) &&
      person.isMustChangePassword) {
      ctx.data.isMustChangePassword = false;
    }
    next();
  });

  Person.beforeRemote('prototype.patchAttributes', (ctx, user, next) => {
    console.log(ctx.methodString, 'was invoked remotely');

    if (ctx.req.params.id && ctx.args.data && ctx.args.data.password) {
      console.log(ctx.args);
      console.log(ctx.req.params);
      Person.findById(ctx.req.params.id, (erroNaBusca, person) => {
        if (person && !person.isEnabled)
          next(new Error('Usuário não está habilitado'));
        else
          next();
      });
    }
    console.log('\n\n\n');
  });

  Person.afterRemote('prototype.patchAttributes', (ctx, user, next) => {
    console.log('After remote method ' + ctx.methodString);
    if (ctx.result)
      console.log(ctx.result);

    console.log('\n\n\n');

    next();
  });
};
