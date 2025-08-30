import { setFormFieldsDisabled } from "../helper/utility.js";
import { UnverifiedUserAPI } from "./api-request-module.js";
import { Message } from "../helper/messages.js";
import Toast from "../helper/toast.js";

const signInTab = document.getElementById("pills_sign_in_tab");
const signUpTab = document.getElementById("pills_sign_up_tab");
const googleBtnText = document.getElementById("google_btn_text");
const facebookBtnText = document.getElementById("facebook_btn_text");
const signInForm = document.getElementById("signin_form");
const signInEmailInput = document.getElementById("signin_email_input");
const signInPasswordInput = document.getElementById("signin_password_input");
const signUpForm = document.getElementById("signup_form");
const signupFormSubmitBtn = document.getElementById("signup_form_submit_btn");
const signUpNameInput = document.getElementById("signup_name_input");
const signUpEmailInput = document.getElementById("signup_email_input");
const signUpPhoneInput = document.getElementById("signup_phone_input");
const termsAndConditionCheckbox = document.getElementById("terms_conditions_checkbox");
const agreeBtn = document.getElementById("agree_terms_btn");
const resetPasswordModal = document.getElementById("reset_password_modal");
const resetPasswordModalInstance = bootstrap.Modal.getOrCreateInstance(resetPasswordModal);
const toggleSecretKey = document.getElementById("toggle_secret_key");
const resetPasswordSecretKey = document.getElementById("reset_password_key");
const resetPasswordNewPassword = document.getElementById("reset_password_new_password");
const resetPasswordConfirmPassword = document.getElementById("reset_password_confirm_password");
const resetPasswordSubmitBtn = document.getElementById("reset_password_submit_btn");
const resetPasswordForm = document.getElementById("reset_password_form");
const secretKeyCollapse = document.getElementById("collapse_secret_key");
const secretKeyCollapseInstance = bootstrap.Collapse.getOrCreateInstance(secretKeyCollapse, {
    toggle: false,
});

// Update button labels safely using textContent
function updateSocialButtons(mode) {
    const action = mode === "signup" ? "up" : "in";
    googleBtnText.textContent = `Sign ${action} with Google`;
    facebookBtnText.textContent = `Sign ${action} with Facebook`;
}

// Bind tab click listeners
function bindTabListeners() {
    signInTab.addEventListener("click", () => updateSocialButtons("signin"));
    signUpTab.addEventListener("click", () => updateSocialButtons("signup"));
}

function validateField(field) {
    if (!field.checkValidity()) {
        field.reportValidity();
    }
}

function handleSignInForm(e) {
    if (!signInForm.checkValidity()) {
        e.preventDefault();
        signInForm.reportValidity();
    }
}

async function handleSignUpForm(e) {
    e.preventDefault();

    if (!signUpForm.checkValidity()) {
        signUpForm.reportValidity();
        return;
    }
    const signUpData = {
        name: signUpNameInput.value.trim(),
        email: signUpEmailInput.value.trim(),
        phoneNumber: signUpPhoneInput.value.trim()
    };

    try {
        setFormFieldsDisabled(signUpForm);

        await UnverifiedUserAPI.createUnverifiedUser(signUpData);
        Toast.success(Message.USER.UNVERIFIED_EMAIL_SENT);
    } catch (e) {
        console.log(e);
    } finally {
        setFormFieldsDisabled(signUpForm, false);
    }
}

function handleSignUpNameInput() {
    signUpNameInput.setCustomValidity("");

    if (!signUpNameInput.checkValidity()) {
        if (signUpNameInput.validity.patternMismatch) {
            signUpNameInput.setCustomValidity("Invalid Name Format:\n" + "• Only letters (a–z, A–Z), spaces, dot (.), and hyphen (-) allowed.\n" + "• Length must be 3–50 characters.");
        }
        signUpNameInput.reportValidity();
    }
}

function handlePhoneNumberInput() {
    signUpPhoneInput.setCustomValidity("");
    if (!signUpPhoneInput.checkValidity()) {
        if (signUpPhoneInput.validity.patternMismatch) {
            signUpPhoneInput.setCustomValidity("Invalid Phone Number:\n• Must be 10 digits\n• Must start with 6, 7, 8, or 9");
        }
        signUpPhoneInput.reportValidity();
    }
}

function resetModal() {
    resetPasswordForm.reset();
    secretKeyCollapseInstance.hide();
}

function handleAgreeButton() {
    termsAndConditionCheckbox.checked = true;
}

function toggleSecretKeyCollapse(show = true) {
    if (show) {
        toggleSecretKey.textContent = "Don't have secret key?";
        resetPasswordSecretKey.disabled = false;
        resetPasswordNewPassword.disabled = false;
        resetPasswordConfirmPassword.disabled = false;
    } else {
        toggleSecretKey.textContent = "Have secret key?";
        resetPasswordSecretKey.disabled = true;
        resetPasswordNewPassword.disabled = true;
        resetPasswordConfirmPassword.disabled = true;
    }
}

function handleNewPasswordInput() {
    resetPasswordNewPassword.setCustomValidity("");

    const isValid = resetPasswordNewPassword.checkValidity();

    if (!isValid) {
        resetPasswordNewPassword.setCustomValidity(`
            Invalid Password Format:
            1. Character limit 8-50
            2. Minimum of 1 uppercase
            3. Minimum of 1 lowercase
            4. Minimum of 1 special character
            5. Minimum of 1 number
        `);
        resetPasswordNewPassword.reportValidity();
    }
}

function handleConfirmPasswordInput() {
    const newPassword = resetPasswordNewPassword.value;
    const confirmPassword = resetPasswordConfirmPassword.value;

    const message = newPassword === confirmPassword ? "" : "Password not matched.";
    resetPasswordConfirmPassword.setCustomValidity(message);
    resetPasswordConfirmPassword.reportValidity();
}

// Export for use in sign-in.js
export {
    bindTabListeners,
    updateSocialButtons,
    signInEmailInput,
    signInPasswordInput,
    validateField,
    signInForm,
    handleSignInForm,
    signUpForm,
    signUpNameInput,
    signUpEmailInput,
    signUpPhoneInput,
    handlePhoneNumberInput,
    termsAndConditionCheckbox,
    handleSignUpForm,
    handleSignUpNameInput,
    handleAgreeButton,
    agreeBtn,
    secretKeyCollapse,
    toggleSecretKeyCollapse,
    resetPasswordModal,
    resetModal,
    resetPasswordNewPassword,
    handleNewPasswordInput,
    resetPasswordConfirmPassword,
    handleConfirmPasswordInput,
};
