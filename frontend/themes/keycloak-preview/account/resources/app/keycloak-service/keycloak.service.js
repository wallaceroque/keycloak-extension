"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var core_1 = require("@angular/core");
// If using a local keycloak.js, uncomment this import.  With keycloak.js fetched
// from the server, you get a compile-time warning on use of the Keycloak()
// method below.  I'm not sure how to fix this, but it's certainly cleaner
// to get keycloak.js from the server.
// 
var Keycloak = require("./keycloak");
var KeycloakService = /** @class */ (function () {
    function KeycloakService() {
    }
    KeycloakService_1 = KeycloakService;
    /**
     * Configure and initialize the Keycloak adapter.
     *
     * @param configOptions Optionally, a path to keycloak.json, or an object containing
     *                      url, realm, and clientId.
     * @param adapterOptions Optional initiaization options.  See javascript adapter docs
     *                       for details.
     * @returns {Promise<T>}
     */
    KeycloakService.init = function (configOptions, initOptions) {
        KeycloakService_1.keycloakAuth = Keycloak(configOptions);
        return new Promise(function (resolve, reject) {
            KeycloakService_1.keycloakAuth.init(initOptions)
                .success(function () {
                resolve();
            })
                .error(function (errorData) {
                reject(errorData);
            });
        });
    };
    KeycloakService.prototype.authenticated = function () {
        return KeycloakService_1.keycloakAuth.authenticated;
    };
    KeycloakService.prototype.login = function () {
        KeycloakService_1.keycloakAuth.login();
    };
    KeycloakService.prototype.logout = function () {
        KeycloakService_1.keycloakAuth.logout();
    };
    KeycloakService.prototype.account = function () {
        KeycloakService_1.keycloakAuth.accountManagement();
    };
    KeycloakService.prototype.authServerUrl = function () {
        return KeycloakService_1.keycloakAuth.authServerUrl;
    };
    KeycloakService.prototype.realm = function () {
        return KeycloakService_1.keycloakAuth.realm;
    };
    KeycloakService.prototype.getToken = function () {
        return new Promise(function (resolve, reject) {
            if (KeycloakService_1.keycloakAuth.token) {
                KeycloakService_1.keycloakAuth
                    .updateToken(5)
                    .success(function () {
                    resolve(KeycloakService_1.keycloakAuth.token);
                })
                    .error(function () {
                    reject('Failed to refresh token');
                });
            }
            else {
                reject('Not loggen in');
            }
        });
    };
    KeycloakService = KeycloakService_1 = __decorate([
        core_1.Injectable()
    ], KeycloakService);
    return KeycloakService;
    var KeycloakService_1;
}());
exports.KeycloakService = KeycloakService;
//# sourceMappingURL=keycloak.service.js.map