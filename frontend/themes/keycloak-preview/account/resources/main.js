"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var platform_browser_dynamic_1 = require("@angular/platform-browser-dynamic");
var app_module_1 = require("./app/app.module");
//import { environment } from './environments/environment';
var keycloak_service_1 = require("./app/keycloak-service/keycloak.service");
var noLogin = false; // convenient for development
if (noLogin) {
    platform_browser_dynamic_1.platformBrowserDynamic().bootstrapModule(app_module_1.AppModule);
}
else {
    keycloak_service_1.KeycloakService.init(authUrl + '/realms/' + realm + '/account/keycloak.json', { onLoad: 'login-required' })
        .then(function () {
        platform_browser_dynamic_1.platformBrowserDynamic().bootstrapModule(app_module_1.AppModule);
    })
        .catch(function (e) {
        console.log('Error in bootstrap: ' + JSON.stringify(e));
    });
}
//# sourceMappingURL=main.js.map