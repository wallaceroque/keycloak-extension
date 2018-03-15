"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
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
var translate_util_1 = require("../ngx-translate/translate.util");
var keycloak_service_1 = require("../keycloak-service/keycloak.service");
var responsiveness_service_1 = require("../responsiveness-service/responsiveness.service");
var referrer_1 = require("../page/referrer");
var TopNavComponent = /** @class */ (function () {
    function TopNavComponent(keycloakService, translateUtil, respSvc) {
        this.keycloakService = keycloakService;
        this.respSvc = respSvc;
        this.resourceUrl = resourceUrl;
        this.referrer = new referrer_1.Referrer(translateUtil);
    }
    TopNavComponent.prototype.menuClicked = function () {
        this.respSvc.menuClicked();
    };
    TopNavComponent.prototype.ngOnInit = function () {
    };
    TopNavComponent.prototype.logout = function () {
        this.keycloakService.logout();
    };
    TopNavComponent = __decorate([
        core_1.Component({
            selector: 'app-top-nav',
            templateUrl: './top-nav.component.html',
            styleUrls: ['./top-nav.component.css']
        }),
        __metadata("design:paramtypes", [keycloak_service_1.KeycloakService, translate_util_1.TranslateUtil, responsiveness_service_1.ResponsivenessService])
    ], TopNavComponent);
    return TopNavComponent;
}());
exports.TopNavComponent = TopNavComponent;
//# sourceMappingURL=top-nav.component.js.map