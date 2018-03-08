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
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("rxjs/Observable");
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var DeclaredVarTranslateLoader = /** @class */ (function () {
    function DeclaredVarTranslateLoader() {
    }
    DeclaredVarTranslateLoader.prototype.getTranslation = function (lang) {
        return Observable_1.Observable.of(l18n_msg);
    };
    return DeclaredVarTranslateLoader;
}());
exports.DeclaredVarTranslateLoader = DeclaredVarTranslateLoader;
//# sourceMappingURL=declared.var.translate.loader.js.map