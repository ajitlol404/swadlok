import { dashboardTab, handleDashboardTabShown, accountTab, handleAccountTabShown } from "./module/dashboard-module.js";

dashboardTab.addEventListener("shown.bs.tab", handleDashboardTabShown);
accountTab.addEventListener("shown.bs.tab", handleAccountTabShown);