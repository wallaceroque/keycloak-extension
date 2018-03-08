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
/**
* Case insensitive filtering.
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var FilterbyPipe = /** @class */ (function () {
    function FilterbyPipe() {
    }
    FilterbyPipe.prototype.transform = function (objects, property, text) {
        if (!property)
            return objects;
        if (!text)
            return objects;
        var transformed = [];
        for (var _i = 0, objects_1 = objects; _i < objects_1.length; _i++) {
            var obj = objects_1[_i];
            var propVal = obj[property];
            if (!this.isString(propVal)) {
                console.error("Can't filter property " + property + ". Its value is not a string.");
                break;
            }
            var strPropVal = propVal;
            if (strPropVal.toLowerCase().indexOf(text.toLowerCase()) != -1) {
                transformed.push(obj);
            }
        }
        return transformed;
    };
    FilterbyPipe.prototype.isString = function (value) {
        return (typeof value == 'string') || (value instanceof String);
    };
    FilterbyPipe = __decorate([
        core_1.Pipe({ name: 'filterby' })
    ], FilterbyPipe);
    return FilterbyPipe;
}());
exports.FilterbyPipe = FilterbyPipe;
//# sourceMappingURL=filterby.pipe.js.map