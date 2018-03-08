"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
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
var core_1 = require("@angular/core");
var app_card_1 = require("./app-card");
var application_1 = require("./application");
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var SmallAppCardComponent = /** @class */ (function (_super) {
    __extends(SmallAppCardComponent, _super);
    function SmallAppCardComponent() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    __decorate([
        core_1.Input(),
        __metadata("design:type", application_1.Application)
    ], SmallAppCardComponent.prototype, "app", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Array)
    ], SmallAppCardComponent.prototype, "sessions", void 0);
    SmallAppCardComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'small-app-card',
            templateUrl: 'small-app-card.component.html',
            styleUrls: ['small-app-card.component.css']
        })
    ], SmallAppCardComponent);
    return SmallAppCardComponent;
}(app_card_1.AppCard));
exports.SmallAppCardComponent = SmallAppCardComponent;
//# sourceMappingURL=small-app-card.component.js.map