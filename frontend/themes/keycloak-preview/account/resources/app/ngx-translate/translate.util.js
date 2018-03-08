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
var core_2 = require("@ngx-translate/core");
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var TranslateUtil = /** @class */ (function () {
    function TranslateUtil(translator) {
        this.translator = translator;
    }
    TranslateUtil.prototype.translate = function (key) {
        // remove Freemarker syntax
        if (key.startsWith('${') && key.endsWith('}')) {
            key = key.substring(2, key.length - 1);
        }
        return this.translator.instant(key);
    };
    TranslateUtil = __decorate([
        core_1.Injectable(),
        __metadata("design:paramtypes", [core_2.TranslateService])
    ], TranslateUtil);
    return TranslateUtil;
}());
exports.TranslateUtil = TranslateUtil;
//# sourceMappingURL=translate.util.js.map