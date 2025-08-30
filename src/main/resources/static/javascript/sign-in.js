import {
    bindTabListeners, updateSocialButtons, signInEmailInput, signInPasswordInput, validateField, signInForm, handleSignInForm, signUpForm,
    signUpNameInput, signUpEmailInput, signUpPhoneInput, handlePhoneNumberInput, termsAndConditionCheckbox, handleSignUpForm, handleSignUpNameInput, handleAgreeButton, agreeBtn,
    secretKeyCollapse, toggleSecretKeyCollapse, resetPasswordModal, resetModal, resetPasswordNewPassword, handleNewPasswordInput,
    resetPasswordConfirmPassword, handleConfirmPasswordInput
} from "./module/sign-in-module.js";

// Init
bindTabListeners();
updateSocialButtons("signin");

// Sign-in live validation
signInEmailInput.addEventListener("input", () => validateField(signInEmailInput));
signInPasswordInput.addEventListener("input", () => validateField(signInPasswordInput));
signInForm.addEventListener("submit", handleSignInForm);

// Sign-up live validation
signUpNameInput.addEventListener("input", handleSignUpNameInput);
signUpEmailInput.addEventListener("input", () => validateField(signUpEmailInput));
signUpPhoneInput.addEventListener("input", () => handlePhoneNumberInput);
termsAndConditionCheckbox.addEventListener("input", () => validateField(termsAndConditionCheckbox));
signUpForm.addEventListener("submit", handleSignUpForm);

agreeBtn.addEventListener("click", handleAgreeButton);

resetPasswordModal.addEventListener("show.bs.modal", resetModal);

resetPasswordNewPassword.addEventListener("input", handleNewPasswordInput);
resetPasswordConfirmPassword.addEventListener("input", handleConfirmPasswordInput);

secretKeyCollapse.addEventListener("show.bs.collapse", () => toggleSecretKeyCollapse(true));
secretKeyCollapse.addEventListener("hide.bs.collapse", () => toggleSecretKeyCollapse(false));
