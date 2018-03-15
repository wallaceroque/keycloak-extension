"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Icon = /** @class */ (function () {
    function Icon(vendor, name) {
        this.vendor = vendor;
        this.name = name;
    }
    Icon.prototype.getClasses = function () {
        return this.vendor + " " + this.vendor + "-" + this.name;
    };
    return Icon;
}());
exports.Icon = Icon;
//# sourceMappingURL=icon.js.map