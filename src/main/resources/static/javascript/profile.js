import { viewProfileBtn, initializeProfileModal, profileForm, handleProfileFormSubmit, initializeProfileName } from "./module/profile-module.js";

initializeProfileName();

viewProfileBtn.addEventListener("click", initializeProfileModal);
profileForm.addEventListener("submit", handleProfileFormSubmit);
