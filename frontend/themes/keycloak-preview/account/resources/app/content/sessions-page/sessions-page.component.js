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
var account_service_1 = require("../../account-service/account.service");
var translate_util_1 = require("../../ngx-translate/translate.util");
var refresh_button_1 = require("../widgets/refresh.button");
var session_1 = require("./session");
var SessionsPageComponent = /** @class */ (function () {
    function SessionsPageComponent(accountSvc, translateUtil) {
        var _this = this;
        this.accountSvc = accountSvc;
        this.translateUtil = translateUtil;
        this.filterLabels = [];
        this.sortLabels = [];
        this.response = [];
        this.sessions = [];
        this.actionButtons = [];
        this.initPropLabels();
        this.actionButtons.push(new LogoutAllButton(accountSvc, translateUtil));
        this.actionButtons.push(new refresh_button_1.RefreshButton(accountSvc, "/sessions", this));
        accountSvc.doGetRequest("/sessions", function (res) { return _this.refresh(res); });
    }
    SessionsPageComponent.prototype.initPropLabels = function () {
        this.filterLabels.push({ prop: "ipAddress", label: "IP" });
        this.sortLabels.push({ prop: "ipAddress", label: "IP" });
        this.sortLabels.push({ prop: "started", label: "Started" });
        this.sortLabels.push({ prop: "lastAccess", label: "Last Access" });
        this.sortLabels.push({ prop: "expires", label: "Expires" });
    };
    SessionsPageComponent.prototype.refresh = function (res) {
        console.log('**** response from account REST API ***');
        console.log(JSON.stringify(res));
        console.log('*** res.json() ***');
        console.log(JSON.stringify(res.json()));
        console.log('***************************************');
        this.response = res.json();
        var newSessions = [];
        for (var _i = 0, _a = res.json(); _i < _a.length; _i++) {
            var session = _a[_i];
            newSessions.push(new session_1.Session(session));
        }
        // reference must change to trigger pipes
        this.sessions = newSessions;
    };
    SessionsPageComponent.prototype.logoutAllSessions = function () {
        var _this = this;
        this.accountSvc.doDelete("/sessions", function (res) { return _this.handleLogoutResponse(res); }, { params: { current: true } }, "Logging out all sessions.");
    };
    SessionsPageComponent.prototype.handleLogoutResponse = function (res) {
        console.log('**** response from account DELETE ***');
        console.log(JSON.stringify(res));
        console.log('***************************************');
    };
    SessionsPageComponent.prototype.ngOnInit = function () {
    };
    SessionsPageComponent = __decorate([
        core_1.Component({
            selector: 'app-sessions-page',
            templateUrl: './sessions-page.component.html',
            styleUrls: ['./sessions-page.component.css']
        }),
        __metadata("design:paramtypes", [account_service_1.AccountServiceClient, translate_util_1.TranslateUtil])
    ], SessionsPageComponent);
    return SessionsPageComponent;
}());
exports.SessionsPageComponent = SessionsPageComponent;
var LogoutAllButton = /** @class */ (function () {
    function LogoutAllButton(accountSvc, translateUtil) {
        this.accountSvc = accountSvc;
        this.label = "Logout All"; //TODO: localize in constructor
        this.tooltip = translateUtil.translate('doLogOutAllSessions');
    }
    LogoutAllButton.prototype.performAction = function () {
        var _this = this;
        this.accountSvc.doDelete("/sessions", function (res) { return _this.handleLogoutResponse(res); }, { params: { current: true } }, "Logging out all sessions.");
    };
    LogoutAllButton.prototype.handleLogoutResponse = function (res) {
        console.log('**** response from account DELETE ***');
        console.log(JSON.stringify(res));
        console.log('***************************************');
    };
    return LogoutAllButton;
}());
//# sourceMappingURL=sessions-page.component.js.map