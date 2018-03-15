"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
/**
* Encapsulate referrer logic.
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var Referrer = /** @class */ (function () {
    function Referrer(translateUtil) {
        this.translateUtil = translateUtil;
    }
    Referrer.prototype.exists = function () {
        return typeof referrer !== "undefined";
    };
    // return a value suitable for parameterized use with ngx-translate
    // example {{'backTo' | translate:referrer.getName()}}
    Referrer.prototype.getName = function () {
        return { param_0: this.translateUtil.translate(referrer) };
    };
    Referrer.prototype.getUri = function () {
        return referrer_uri;
    };
    return Referrer;
}());
exports.Referrer = Referrer;
//# sourceMappingURL=referrer.js.map