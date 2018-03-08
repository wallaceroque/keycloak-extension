"use strict";
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
var router_1 = require("@angular/router");
var keycloak_service_1 = require("../keycloak-service/keycloak.service");
var translate_util_1 = require("../ngx-translate/translate.util");
var side_nav_item_1 = require("../page/side-nav-item");
var icon_1 = require("../page/icon");
var responsiveness_service_1 = require("../responsiveness-service/responsiveness.service");
var media_1 = require("../responsiveness-service/media");
var referrer_1 = require("../page/referrer");
var SideNavComponent = /** @class */ (function () {
    function SideNavComponent(router, translateUtil, respSvc, keycloakService) {
        var _this = this;
        this.router = router;
        this.translateUtil = translateUtil;
        this.respSvc = respSvc;
        this.keycloakService = keycloakService;
        this.sideNavClasses = this.respSvc.calcSideNavWidthClasses();
        this.isFirstRouterEvent = true;
        this.referrer = new referrer_1.Referrer(translateUtil);
        this.navItems = [
            this.makeSideNavItem("account", new icon_1.Icon("pficon", "user"), "active"),
            this.makeSideNavItem("password", new icon_1.Icon("pficon", "key")),
            this.makeSideNavItem("authenticator", new icon_1.Icon("pficon", "cloud-security")),
            this.makeSideNavItem("sessions", new icon_1.Icon("fa", "clock-o")),
            this.makeSideNavItem("applications", new icon_1.Icon("fa", "th"))
        ];
        this.router.events.subscribe(function (value) {
            if (value instanceof router_1.NavigationEnd) {
                var navEnd = value;
                _this.setActive(navEnd.url);
                var media = new media_1.Media();
                if (media.isSmall() && !_this.isFirstRouterEvent) {
                    _this.respSvc.menuClicked();
                }
                _this.isFirstRouterEvent = false;
            }
        });
        this.respSvc.addMenuClickListener(this);
    }
    // use itemName for translate key, link, and tooltip
    SideNavComponent.prototype.makeSideNavItem = function (itemName, icon, active) {
        var localizedName = this.translateUtil.translate(itemName);
        return new side_nav_item_1.SideNavItem(localizedName, itemName, localizedName, icon, active);
    };
    SideNavComponent.prototype.logout = function () {
        this.keycloakService.logout();
    };
    SideNavComponent.prototype.menuClicked = function () {
        this.sideNavClasses = this.respSvc.calcSideNavWidthClasses();
    };
    SideNavComponent.prototype.onResize = function (event) {
        this.sideNavClasses = this.respSvc.calcSideNavWidthClasses();
    };
    SideNavComponent.prototype.setActive = function (url) {
        for (var _i = 0, _a = this.navItems; _i < _a.length; _i++) {
            var navItem = _a[_i];
            if (("/" + navItem.link) === url) {
                navItem.setActive("active");
            }
            else {
                navItem.setActive("");
            }
        }
        if ("/" === url) {
            this.navItems[0].setActive("active");
        }
    };
    SideNavComponent.prototype.ngOnInit = function () {
    };
    __decorate([
        core_1.HostListener('window:resize', ['$event']),
        __metadata("design:type", Function),
        __metadata("design:paramtypes", [Object]),
        __metadata("design:returntype", void 0)
    ], SideNavComponent.prototype, "onResize", null);
    SideNavComponent = __decorate([
        core_1.Component({
            selector: 'app-side-nav',
            templateUrl: './side-nav.component.html',
            styleUrls: ['./side-nav.component.css']
        }),
        __metadata("design:paramtypes", [router_1.Router,
            translate_util_1.TranslateUtil,
            responsiveness_service_1.ResponsivenessService,
            keycloak_service_1.KeycloakService])
    ], SideNavComponent);
    return SideNavComponent;
}());
exports.SideNavComponent = SideNavComponent;
//# sourceMappingURL=side-nav.component.js.map