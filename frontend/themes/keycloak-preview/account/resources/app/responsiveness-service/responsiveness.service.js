"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
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
var media_1 = require("./media");
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var ResponsivenessService = /** @class */ (function () {
    function ResponsivenessService() {
        this.menuOn = false;
        this.menuListeners = Array();
    }
    ResponsivenessService.prototype.addMenuClickListener = function (listener) {
        this.menuListeners.push(listener);
    };
    ResponsivenessService.prototype.menuClicked = function () {
        this.menuOn = !this.menuOn;
        for (var _i = 0, _a = this.menuListeners; _i < _a.length; _i++) {
            var listener = _a[_i];
            listener.menuClicked();
        }
    };
    ResponsivenessService.prototype.calcSideNavWidthClasses = function () {
        var media = new media_1.Media();
        if (media.isLarge() && !this.menuOn) {
            return "";
        }
        if (media.isLarge() && this.menuOn) {
            return "collapsed";
        }
        if (media.isMedium() && !this.menuOn) {
            return "collapsed";
        }
        if (media.isMedium() && this.menuOn) {
            return "";
        }
        // media must be small
        if (!this.menuOn) {
            return "hidden";
        }
        return "hidden show-mobile-nav";
    };
    ResponsivenessService.prototype.calcSideContentWidthClass = function () {
        var media = new media_1.Media();
        if (media.isLarge() && !this.menuOn) {
            return "";
        }
        if (media.isLarge() && this.menuOn) {
            return "collapsed-nav";
        }
        if (media.isMedium() && !this.menuOn) {
            return "collapsed-nav";
        }
        if (media.isMedium() && this.menuOn) {
            return "";
        }
        // media must be small
        if (!this.menuOn) {
            return "hidden-nav";
        }
        return "hidden-nav";
    };
    ResponsivenessService = __decorate([
        core_1.Injectable()
    ], ResponsivenessService);
    return ResponsivenessService;
}());
exports.ResponsivenessService = ResponsivenessService;
//# sourceMappingURL=responsiveness.service.js.map