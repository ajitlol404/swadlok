import { adminPasswordInput, handleAdminPasswordInput, adminPhoneInput, handleAdminPhoneInput, adminSetupForm, handleAdminSetupFormSubmit } from "./module/setup-module.js";

adminPasswordInput.addEventListener("input", handleAdminPasswordInput);
adminPhoneInput.addEventListener("input", handleAdminPhoneInput);
adminSetupForm.addEventListener("submit", handleAdminSetupFormSubmit);