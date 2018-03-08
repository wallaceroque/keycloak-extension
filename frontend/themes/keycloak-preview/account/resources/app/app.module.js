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
var platform_browser_1 = require("@angular/platform-browser");
var core_1 = require("@angular/core");
var forms_1 = require("@angular/forms");
var http_1 = require("@angular/http");
var router_1 = require("@angular/router");
var common_1 = require("@angular/common");
var core_2 = require("@ngx-translate/core");
var core_3 = require("@ngx-translate/core");
var keycloak_service_1 = require("./keycloak-service/keycloak.service");
var keycloak_http_1 = require("./keycloak-service/keycloak.http");
var responsiveness_service_1 = require("./responsiveness-service/responsiveness.service");
var account_service_1 = require("./account-service/account.service");
var translate_util_1 = require("./ngx-translate/translate.util");
var declared_var_translate_loader_1 = require("./ngx-translate/declared.var.translate.loader");
var app_component_1 = require("./app.component");
var top_nav_component_1 = require("./top-nav/top-nav.component");
var notification_component_1 = require("./top-nav/notification.component");
var toast_notifier_1 = require("./top-nav/toast.notifier");
var side_nav_component_1 = require("./side-nav/side-nav.component");
var account_page_component_1 = require("./content/account-page/account-page.component");
var password_page_component_1 = require("./content/password-page/password-page.component");
var page_not_found_component_1 = require("./content/page-not-found/page-not-found.component");
var authenticator_page_component_1 = require("./content/authenticator-page/authenticator-page.component");
var sessions_page_component_1 = require("./content/sessions-page/sessions-page.component");
var large_session_card_component_1 = require("./content/sessions-page/large-session-card.component");
var small_session_card_component_1 = require("./content/sessions-page/small-session-card.component");
var applications_page_component_1 = require("./content/applications-page/applications-page.component");
var large_app_card_component_1 = require("./content/applications-page/large-app-card.component");
var small_app_card_component_1 = require("./content/applications-page/small-app-card.component");
var row_app_card_component_1 = require("./content/applications-page/row-app-card.component");
var toolbar_component_1 = require("./content/widgets/toolbar.component");
var orderby_pipe_1 = require("./pipes/orderby.pipe");
var filterby_pipe_1 = require("./pipes/filterby.pipe");
var routes = [
    { path: 'account', component: account_page_component_1.AccountPageComponent },
    { path: 'password', component: password_page_component_1.PasswordPageComponent },
    { path: 'authenticator', component: authenticator_page_component_1.AuthenticatorPageComponent },
    { path: 'sessions', component: sessions_page_component_1.SessionsPageComponent },
    { path: 'applications', component: applications_page_component_1.ApplicationsPageComponent },
    { path: '', redirectTo: '/account', pathMatch: 'full' },
    { path: '**', component: page_not_found_component_1.PageNotFoundComponent }
];
var decs = [
    app_component_1.AppComponent,
    top_nav_component_1.TopNavComponent,
    notification_component_1.NotificationComponent,
    side_nav_component_1.SideNavComponent,
    account_page_component_1.AccountPageComponent,
    password_page_component_1.PasswordPageComponent,
    page_not_found_component_1.PageNotFoundComponent,
    authenticator_page_component_1.AuthenticatorPageComponent,
    sessions_page_component_1.SessionsPageComponent,
    large_session_card_component_1.LargeSessionCardComponent,
    small_session_card_component_1.SmallSessionCardComponent,
    applications_page_component_1.ApplicationsPageComponent,
    large_app_card_component_1.LargeAppCardComponent,
    small_app_card_component_1.SmallAppCardComponent,
    row_app_card_component_1.RowAppCardComponent,
    toolbar_component_1.ToolbarComponent,
    orderby_pipe_1.OrderbyPipe,
    filterby_pipe_1.FilterbyPipe
];
exports.ORIGINAL_INCOMING_URL = window.location;
var AppModule = /** @class */ (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        core_1.NgModule({
            declarations: decs,
            imports: [
                platform_browser_1.BrowserModule,
                forms_1.FormsModule,
                http_1.HttpModule,
                core_3.TranslateModule.forRoot({
                    loader: { provide: core_2.TranslateLoader, useClass: declared_var_translate_loader_1.DeclaredVarTranslateLoader }
                }),
                router_1.RouterModule.forRoot(routes)
            ],
            providers: [
                keycloak_service_1.KeycloakService,
                keycloak_http_1.KEYCLOAK_HTTP_PROVIDER,
                responsiveness_service_1.ResponsivenessService,
                account_service_1.AccountServiceClient,
                translate_util_1.TranslateUtil,
                toast_notifier_1.ToastNotifier,
                { provide: common_1.LocationStrategy, useClass: common_1.HashLocationStrategy }
            ],
            bootstrap: [app_component_1.AppComponent]
        })
    ], AppModule);
    return AppModule;
}());
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map