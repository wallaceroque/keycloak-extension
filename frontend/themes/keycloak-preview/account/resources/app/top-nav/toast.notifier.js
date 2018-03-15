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
var core_1 = require("@angular/core");
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var ToastNotifier = /** @class */ (function (_super) {
    __extends(ToastNotifier, _super);
    function ToastNotifier() {
        return _super.call(this) || this;
    }
    ToastNotifier = __decorate([
        core_1.Injectable(),
        __metadata("design:paramtypes", [])
    ], ToastNotifier);
    return ToastNotifier;
}(core_1.EventEmitter));
exports.ToastNotifier = ToastNotifier;
var ToastNotification = /** @class */ (function () {
    function ToastNotification(message, messageType) {
        this.message = message;
        this.alertType = "alert-success";
        this.icon = "pficon-ok";
        switch (messageType) {
            case "info": {
                this.alertType = "alert-info";
                this.icon = "pficon-info";
                break;
            }
            case "warning": {
                this.alertType = "alert-warning";
                this.icon = "pficon-warning-triangle-o";
                break;
            }
            case "error": {
                this.alertType = "alert-danger";
                this.icon = "pficon-error-circle-o";
                break;
            }
            default: {
                this.alertType = "alert-success";
                this.icon = "pficon-ok";
            }
        }
    }
    return ToastNotification;
}());
exports.ToastNotification = ToastNotification;
//# sourceMappingURL=toast.notifier.js.map