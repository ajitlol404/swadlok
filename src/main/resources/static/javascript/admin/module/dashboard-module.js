import { addAccountBtn, initializeAccountModal, accountForm, handleAccountFormSubmit, initializeAccountTable } from "./account-module.js";
import { addAccountBtn, initializeAccountModal, accountForm, handleAccountFormSubmit, initializeAccountTable } from "./merchant-module.js";

const dashboardTab = document.getElementById("dashboard_tab");
const accountTab = document.getElementById("account_tab");
const merchantTab = document.getElementById("merchant_tab");

// Dashboard
function handleDashboardTabShown(event) {
    document.title = "PN - ADM - Dashboard";
}

// Account
function handleAccountTabShown(event) {
    document.title = "PN - ADM - Account";
    initializeAccountTable();
}

addAccountBtn.addEventListener("click", initializeAccountModal);
accountForm.addEventListener("submit", handleAccountFormSubmit);

export { dashboardTab, accountTab, merchantTab, handleDashboardTabShown, handleAccountTabShown };
