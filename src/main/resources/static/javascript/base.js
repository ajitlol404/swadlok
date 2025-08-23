import { toastAlert, toastAlertInstance, signOutBtn, handleSignOut } from "./module/base-module.js"

if (toastAlert) {
    toastAlertInstance.show();
}

signOutBtn?.addEventListener("click", handleSignOut);
