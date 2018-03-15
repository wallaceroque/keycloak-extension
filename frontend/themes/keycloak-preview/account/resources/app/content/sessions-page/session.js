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
var Session = /** @class */ (function () {
    function Session(session) {
        this.session = session;
    }
    Object.defineProperty(Session.prototype, "ipAddress", {
        get: function () {
            return this.session.ipAddress;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Session.prototype, "started", {
        get: function () {
            return this.session.started;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Session.prototype, "lastAccess", {
        get: function () {
            return this.session.lastAccess;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Session.prototype, "expires", {
        get: function () {
            return this.session.expires;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Session.prototype, "clients", {
        get: function () {
            return this.session.clients;
        },
        enumerable: true,
        configurable: true
    });
    return Session;
}());
exports.Session = Session;
//# sourceMappingURL=session.js.map