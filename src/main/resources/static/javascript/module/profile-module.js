import { setFormFieldsDisabled, setButtonLoading, toggleLoading } from "../helper/utility.js";
import { ProfileAPI } from "../module/api-request-module.js";
import Toast from "../helper/toast.js";
import { Message } from "../helper/messages.js";

const adminDropdownBtn = document.getElementById("admin_dropdown_btn");
const viewProfileBtn = document.getElementById("view_profile_btn");
const profileModal = document.getElementById("profile_modal");
const profileModalInstance = bootstrap.Modal.getOrCreateInstance(profileModal);
const profileForm = document.getElementById("profile_form");
const profileIdInput = document.getElementById("profile_id_input");
const profileNameInput = document.getElementById("profile_name_input");
const profileEmailInput = document.getElementById("profile_email_input");
const profilePasswordInput = document.getElementById("profile_password_input");
const profileRoleInput = document.getElementById("profile_role_input");
const profileStatusInput = document.getElementById("profile_status_input");

const profileNameEl = document.getElementById("profile_name");

async function initializeProfileModal() {
    profileForm.reset();
    profileIdInput.disabled = true;
    profileEmailInput.disabled = true;
    profileRoleInput.disabled = true;
    profileStatusInput.disabled = true;

    ProfileAPI.getProfile()
        .then((profile) => {
            profileIdInput.value = profile.code;
            profileNameInput.value = profile.name;
            profileEmailInput.value = profile.email;
            profileRoleInput.value = profile.role;
            profileStatusInput.checked = profile.active;
            profilePasswordInput.value = "";

            profileModalInstance.show();
            profileForm.dataset.editKey = profile.id;
        })
        .catch((e) => {
            console.error("Failed to load profile", e);
        });
}

async function handleProfileFormSubmit(e) {
    e.preventDefault();

    const body = {
        name: profileNameInput.value.trim(),
        password: profilePasswordInput.value || null,
    };

    try {
        setFormFieldsDisabled(profileForm);

        await ProfileAPI.updateProfile(body);
        Toast.success(Message.PROFILE.UPDATED);

        // update dropdown "Hi, {name}" live
        if (profileNameEl) {
            profileNameEl.textContent = profileNameInput.value;
        }

        profileModalInstance.hide();
    } catch (e) {
        console.error(e);
    } finally {
        setFormFieldsDisabled(profileForm, false);
    }
}

function initializeProfileName() {
    toggleLoading(adminDropdownBtn);

    ProfileAPI.getProfile()
        .then((profile) => {
            if (profileNameEl) {
                profileNameEl.textContent = profile.name;
            }
        })
        .catch((e) => {
            console.error("Failed to load profile name", e);
        })
        .finally(() => {
            toggleLoading(adminDropdownBtn, false);
        });
}

export { viewProfileBtn, initializeProfileModal, profileForm, handleProfileFormSubmit, initializeProfileName };
