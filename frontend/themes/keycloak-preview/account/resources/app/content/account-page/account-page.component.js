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
var forms_1 = require("@angular/forms");
var account_service_1 = require("../../account-service/account.service");
var AccountPageComponent = /** @class */ (function () {
    function AccountPageComponent(accountSvc) {
        var _this = this;
        this.accountSvc = accountSvc;
        accountSvc.doGetRequest("/", function (res) { return _this.handleGetResponse(res); });
    }
    AccountPageComponent.prototype.saveAccount = function () {
        var _this = this;
        console.log("posting: " + JSON.stringify(this.formGroup.value));
        this.accountSvc.doPostRequest("/", function (res) { return _this.handlePostResponse(res); }, this.formGroup.value);
        this.formGroup.reset(this.formGroup.value);
    };
    AccountPageComponent.prototype.handleGetResponse = function (res) {
        var response = res.json();
        this.username = response.username;
        this.formGroup.reset(response);
        console.log('**** response from account REST API ***');
        console.log(JSON.stringify(res));
        console.log('*** formGroup ***');
        console.log(JSON.stringify(this.formGroup.value));
        console.log('***************************************');
    };
    AccountPageComponent.prototype.handlePostResponse = function (res) {
        console.log('**** response from account POST ***');
        console.log(JSON.stringify(res));
        console.log('***************************************');
    };
    AccountPageComponent.prototype.ngOnInit = function () {
    };
    __decorate([
        core_1.ViewChild('formGroup'),
        __metadata("design:type", forms_1.FormGroup)
    ], AccountPageComponent.prototype, "formGroup", void 0);
    AccountPageComponent = __decorate([
        core_1.Component({
            selector: 'app-account-page',
            templateUrl: './account-page.component.html',
            styleUrls: ['./account-page.component.css']
        }),
        __metadata("design:paramtypes", [account_service_1.AccountServiceClient])
    ], AccountPageComponent);
    return AccountPageComponent;
}());
exports.AccountPageComponent = AccountPageComponent;
//# sourceMappingURL=account-page.component.js.map