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
var AppCard = /** @class */ (function () {
    function AppCard() {
    }
    /*
        protected getName(): string {
            if (this.app.hasOwnProperty('name')) {
                return this.translateUtil.translate(this.app.name);
            }
            
            return this.app.clientId;
        }
        
        protected getDescription (): string {
            if (!this.app.hasOwnProperty('description')) return null;
            
            let desc: string = this.app.description;
            
            if (desc.indexOf("//icon") > -1) {
                desc = desc.substring(0, desc.indexOf("//icon"));
            }
            
            return desc;
        }*/
    AppCard.prototype.isSessionActive = function () {
        for (var _i = 0, _a = this.sessions; _i < _a.length; _i++) {
            var session = _a[_i];
            for (var _b = 0, _c = session.clients; _b < _c.length; _b++) {
                var client = _c[_b];
                if (this.app.clientId === client.clientId)
                    return true;
            }
        }
        return false;
    };
    return AppCard;
}());
exports.AppCard = AppCard;
//# sourceMappingURL=app-card.js.map