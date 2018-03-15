"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var SideNavItem = /** @class */ (function () {
    function SideNavItem(displayName, link, tooltip, icon, active) {
        this.displayName = displayName;
        this.link = link;
        this.tooltip = tooltip;
        this.icon = icon;
        this.active = active;
    }
    SideNavItem.prototype.setActive = function (active) {
        this.active = active;
    };
    return SideNavItem;
}());
exports.SideNavItem = SideNavItem;
//# sourceMappingURL=side-nav-item.js.map