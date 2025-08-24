import { setFormFieldsDisabled, setButtonLoading, toggleLoading } from "../helper/utility.js";
import { SetupAPI } from "../module/api-request-module.js";
import Toast from "../helper/toast.js";
import { Message } from "../helper/messages.js";

const adminSetupForm = document.getElementById("admin_setup_form");
const adminNameInput = document.getElementById("admin_name_input");
const adminEmailInput = document.getElementById("admin_email_input");
const adminPasswordInput = document.getElementById("admin_password_input");
const adminPhoneInput = document.getElementById("admin_phone_input");
const adminImageInput = document.getElementById("admin_image_input");
const adminSetupFormSubmitBtn = document.getElementById("admin_setup_form_submit_btn");

function handleAdminPasswordInput() {
    adminPasswordInput.setCustomValidity("");

    if (!adminPasswordInput.checkValidity()) {
        if (adminPasswordInput.validity.patternMismatch) {
            adminPasswordInput.setCustomValidity(
                "Invalid Password Format:\n" +
                    "• 8–32 characters long\n" +
                    "• At least one uppercase letter (A–Z)\n" +
                    "• At least one lowercase letter (a–z)\n" +
                    "• At least one digit (0–9)\n" +
                    "• At least one special character (!@#$%^&*)\n" +
                    "• No spaces allowed"
            );
        }
        adminPasswordInput.reportValidity();
    }
}

function handleAdminPhoneInput() {
    adminPhoneInput.setCustomValidity("");

    if (!adminPhoneInput.checkValidity()) {
        if (adminPhoneInput.validity.patternMismatch) {
            adminPhoneInput.setCustomValidity("Invalid Phone Number:\n" + "• Must be exactly 10 digits\n" + "• Must start with 6, 7, 8, or 9\n" + "• Do not include country code (e.g., +91)");
        }
        adminPhoneInput.reportValidity();
    }
}

async function handleAdminSetupFormSubmit(e) {
    e.preventDefault();

    const body = {
        name: adminNameInput.value.trim(),
        email: adminEmailInput.value.trim(),
        password: adminPasswordInput.value.trim(),
        phone: adminPhoneInput.value.trim(),
    };

    try {
        setFormFieldsDisabled(adminSetupForm);

        let uploadedImageId = null;

        if (adminImageInput.files.length > 0) {
            const file = adminImageInput.files[0];
            const uploadResult = await SetupAPI.uploadImage("USER", file);
            uploadedImageId = uploadResult.fileId;
        }

        await SetupAPI.createAdmin(body);
        Toast.success(Message.USER.ADMIN_CREATED);

    } catch (e) {
        console.error(e);
    } finally {
        setFormFieldsDisabled(adminSetupForm, false);
    }
}

export { adminPasswordInput, handleAdminPasswordInput, adminPhoneInput, handleAdminPhoneInput, adminSetupForm, handleAdminSetupFormSubmit };
