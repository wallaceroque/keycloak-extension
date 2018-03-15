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
var translate_util_1 = require("../../ngx-translate/translate.util");
var account_service_1 = require("../../account-service/account.service");
var application_1 = require("./application");
var refresh_button_1 = require("../widgets/refresh.button");
var ApplicationsPageComponent = /** @class */ (function () {
    function ApplicationsPageComponent(accountSvc, translateUtil) {
        var _this = this;
        this.translateUtil = translateUtil;
        this.activeView = "LargeCards";
        this.resourceUrl = resourceUrl;
        this.applications = [];
        this.isSortAscending = true;
        this.sortBy = "name";
        this.filterBy = "name";
        this.filterText = "";
        this.propLabels = [];
        this.actionButtons = [];
        this.sessions = [];
        this.initPropLabels();
        this.actionButtons.push(new refresh_button_1.RefreshButton(accountSvc, "/applications", this));
        accountSvc.doGetRequest("/applications", function (res) { return _this.refresh(res); });
        accountSvc.doGetRequest("/sessions", function (res) { return _this.handleGetSessionsResponse(res); });
    }
    ApplicationsPageComponent.prototype.initPropLabels = function () {
        this.propLabels.push({ prop: "name", label: "Name" });
        this.propLabels.push({ prop: "description", label: "Description" });
    };
    ApplicationsPageComponent.prototype.refresh = function (res) {
        console.log('**** response from apps REST API ***');
        console.log(JSON.stringify(res));
        console.log('*** apps res.json() ***');
        console.log(JSON.stringify(res.json().applications));
        console.log('*************************************');
        var newApps = [];
        for (var _i = 0, _a = res.json().applications; _i < _a.length; _i++) {
            var app = _a[_i];
            newApps.push(new application_1.Application(app, this.translateUtil));
        }
        // reference must change to trigger pipes
        this.applications = newApps;
    };
    ApplicationsPageComponent.prototype.handleGetSessionsResponse = function (res) {
        console.log('**** response from sessions REST API ***');
        console.log(JSON.stringify(res));
        console.log('*** sessions res.json() ***');
        console.log(JSON.stringify(res.json()));
        console.log('***************************************');
        this.sessions = res.json();
    };
    ApplicationsPageComponent.prototype.ngOnInit = function () {
    };
    ApplicationsPageComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-applications-page',
            templateUrl: 'applications-page.component.html',
            styleUrls: ['applications-page.component.css']
        }),
        __metadata("design:paramtypes", [account_service_1.AccountServiceClient, translate_util_1.TranslateUtil])
    ], ApplicationsPageComponent);
    return ApplicationsPageComponent;
}());
exports.ApplicationsPageComponent = ApplicationsPageComponent;
//# sourceMappingURL=applications-page.component.js.map