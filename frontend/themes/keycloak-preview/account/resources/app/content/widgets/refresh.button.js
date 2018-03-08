"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var icon_1 = require("../../page/icon");
/**
*
* @author Stan Silvert ssilvert@redhat.com (C) 2017 Red Hat Inc.
*/
var RefreshButton = /** @class */ (function () {
    function RefreshButton(accountSvc, request, refreshable) {
        this.accountSvc = accountSvc;
        this.request = request;
        this.refreshable = refreshable;
        this.label = new icon_1.Icon('fa', 'refresh');
        this.tooltip = 'Refresh'; //TODO: localize in constructor
    }
    RefreshButton.prototype.performAction = function () {
        var _this = this;
        this.accountSvc.doGetRequest(this.request, function (res) {
            _this.refreshable.refresh(res);
        });
    };
    return RefreshButton;
}());
exports.RefreshButton = RefreshButton;
//# sourceMappingURL=refresh.button.js.map