"use strict";
/*
 * Copyright 2017 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
var core_1 = require("@angular/core");
var http_1 = require("@angular/http");
var toast_notifier_1 = require("../top-nav/toast.notifier");
var keycloak_service_1 = require("../keycloak-service/keycloak.service");
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var AccountServiceClient = /** @class */ (function () {
    function AccountServiceClient(http, kcSvc, notifier) {
        this.http = http;
        this.kcSvc = kcSvc;
        this.notifier = notifier;
        this.accountUrl = kcSvc.authServerUrl() + 'realms/' + kcSvc.realm() + '/account';
    }
    AccountServiceClient.prototype.doGetRequest = function (endpoint, responseHandler, options) {
        var _this = this;
        this.http.get(this.accountUrl + endpoint, options)
            .subscribe(function (res) { return responseHandler(res); }, function (error) { return _this.handleServiceError(error); });
    };
    AccountServiceClient.prototype.doPostRequest = function (endpoint, responseHandler, options, successMessage) {
        var _this = this;
        this.http.post(this.accountUrl + endpoint, options)
            .subscribe(function (res) { return _this.handleAccountUpdated(responseHandler, res, successMessage); }, function (error) { return _this.handleServiceError(error); });
    };
    AccountServiceClient.prototype.handleAccountUpdated = function (responseHandler, res, successMessage) {
        var message = "Your account has been updated.";
        if (successMessage)
            message = successMessage;
        this.notifier.emit(new toast_notifier_1.ToastNotification(message, "success"));
        responseHandler(res);
    };
    AccountServiceClient.prototype.doDelete = function (endpoint, responseHandler, options, successMessage) {
        var _this = this;
        this.http.delete(this.accountUrl + endpoint, options)
            .subscribe(function (res) { return _this.handleAccountUpdated(responseHandler, res, successMessage); }, function (error) { return _this.handleServiceError(error); });
    };
    AccountServiceClient.prototype.handleServiceError = function (response) {
        console.log('**** ERROR!!!! ***');
        console.log(JSON.stringify(response));
        console.log("response.status=" + response.status);
        console.log('***************************************');
        if ((response.status === undefined) || (response.status === 401)) {
            this.kcSvc.logout();
            return;
        }
        if (response.status === 403) {
            // TODO: go to a forbidden page?
        }
        if (response.status === 404) {
            // TODO: route to PageNotFoundComponent
        }
        var message = response.status + " " + response.statusText;
        var not500Error = response.status !== 500;
        console.log('not500Error=' + not500Error);
        // Unfortunately, errors can be sent back in the response body as
        // 'errorMessage' or 'error_description'
        if (not500Error && response.json().hasOwnProperty('errorMessage')) {
            message = response.json().errorMessage;
        }
        if (not500Error && response.json().hasOwnProperty('error_description')) {
            message = response.json().error_description;
        }
        this.notifier.emit(new toast_notifier_1.ToastNotification(message, "error"));
    };
    AccountServiceClient = __decorate([
        core_1.Injectable(),
        __metadata("design:paramtypes", [http_1.Http,
            keycloak_service_1.KeycloakService,
            toast_notifier_1.ToastNotifier])
    ], AccountServiceClient);
    return AccountServiceClient;
}());
exports.AccountServiceClient = AccountServiceClient;
//# sourceMappingURL=account.service.js.map