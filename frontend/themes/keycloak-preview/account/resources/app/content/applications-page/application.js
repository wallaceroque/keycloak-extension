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
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var Application = /** @class */ (function () {
    function Application(app, translateUtil) {
        this.app = app;
        this.translateUtil = translateUtil;
        this.setIcon();
    }
    Application.prototype.setIcon = function () {
        this.app.icon = "pficon-key";
        if (!this.app.hasOwnProperty('description')) {
            return;
        }
        var desc = this.app.description;
        var iconIndex = desc.indexOf("//icon=");
        if (iconIndex > -1) {
            this.app.icon = desc.substring(iconIndex + 7, desc.length);
        }
    };
    Object.defineProperty(Application.prototype, "clientId", {
        get: function () {
            return this.app.name;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Application.prototype, "name", {
        get: function () {
            if (this.app.hasOwnProperty('name')) {
                return this.translateUtil.translate(this.app.name);
            }
            return this.app.clientId;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Application.prototype, "description", {
        get: function () {
            if (!this.app.hasOwnProperty('description'))
                return null;
            var desc = this.app.description;
            if (desc.indexOf("//icon") > -1) {
                desc = desc.substring(0, desc.indexOf("//icon"));
            }
            return desc;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Application.prototype, "icon", {
        get: function () {
            return this.app.icon;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Application.prototype, "effectiveUrl", {
        get: function () {
            return this.app.effectiveUrl;
        },
        enumerable: true,
        configurable: true
    });
    return Application;
}());
exports.Application = Application;
//# sourceMappingURL=application.js.map