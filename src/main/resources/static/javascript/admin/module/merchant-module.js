import { setFormFieldsDisabled, formatDateTime, DATATABLE_DEFAULT_OPTIONS, getPenIcon, getTrashIcon, setupDefaultOption, addSelectOption } from "../../helper/utility.js";
import { BASE_URLS, AccountAPI, BankAPI, CommonAPI } from "../../module/api-request-module.js";
import Toast from "../../helper/toast.js";
import { Message } from "../../helper/messages.js";

const merchantModal = document.getElementById("merchant_modal");
const merchantModalInstance = bootstrap.Modal.getOrCreateInstance(merchantModal);
const addMerchantBtn = document.getElementById("add_merchant_btn");
const merchantForm = document.getElementById("merchant_form");

const merchantBusinessNameInput = document.getElementById("merchant_business_name");
const merchantGstNumberInput = document.getElementById("merchant_gst_number");
const merchantContactNumberInput = document.getElementById("merchant_contact_number");
const merchantSuccessUrlInput = document.getElementById("merchant_success_url");
const merchantFailureUrlInput = document.getElementById("merchant_failure_url");
const merchantThankYouUrlInput = document.getElementById("merchant_thank_you_url");
const merchantRedirectUrlInput = document.getElementById("merchant_redirect_url");
const merchantCallbackUrlInput = document.getElementById("merchant_callback_url");
const merchantIpWhitelistInput = document.getElementById("merchant_ip_whitelist");

const merchantAccountsSelect = document.getElementById("merchant_accounts");
const merchantServicesSelect = document.getElementById("merchant_services");

let merchantDatatable;

function initializeMerchantTable() {
    if (merchantDatatable) return merchantDatatable;

    merchantDatatable = new DataTable("#merchant_table", {
        ...DATATABLE_DEFAULT_OPTIONS,
        language: {
            ...DATATABLE_DEFAULT_OPTIONS.language,
            searchPlaceholder: "Search by merchant ID, name, or email...",
        },
        serverSide: true,
        ajax: {
            ...DATATABLE_DEFAULT_OPTIONS.ajax,
            url: `${BASE_URLS.MERCHANT}/datatable`,
            dataSrc: "data",
            error: function (xhr) {
                Toast.danger("Failed to load merchants");
                if (xhr.status >= 400 && xhr.status < 600) {
                    const response = xhr.responseJSON;
                    const message = response ? response.message : "An error occurred";
                    Toast.danger(message);
                }
            },
        },
        order: [[4, "desc"]],
        columnDefs: [
            { defaultContent: "-", targets: "_all" },
            { targets: 0, data: "id" },
            { targets: 1, data: "businessName" },
            { targets: 2, data: "email" },
            { targets: 3, data: "gstNumber" },
            { targets: 4, data: "updatedAt", render: (data) => formatDateTime(data) },
            {
                targets: 5,
                data: "id",
                orderable: false,
                className: "text-center",
                createdCell: function (td, cellData, rowData) {
                    td.textContent = "";

                    const editSpan = document.createElement("span");
                    editSpan.className = "text-warning me-3 cursor-pointer";
                    editSpan.appendChild(getPenIcon());
                    editSpan.addEventListener("click", () => initializeEditMerchantModal(rowData.id));

                    const deleteSpan = document.createElement("span");
                    deleteSpan.className = "text-danger cursor-pointer";
                    deleteSpan.appendChild(getTrashIcon());
                    deleteSpan.addEventListener("click", () => handleDeleteMerchant(rowData.id));

                    td.replaceChildren(editSpan, deleteSpan);
                },
            },
        ],
    });

    return merchantDatatable;
}

function initializeMerchantModal() {
    merchantForm.reset();
    merchantForm.dataset.method = "POST";
    delete merchantForm.dataset.editKey;

    // Reset dropdowns
    merchantAccountsSelect.length = 0;
    merchantServicesSelect.length = 0;
    setupDefaultOption(merchantAccountsSelect, "Select accounts");
    setupDefaultOption(merchantServicesSelect, "Select services");

    // Populate accounts
    AccountAPI.getAllAccounts()
        .then((accounts) => {
            accounts.forEach((a) => {
                addSelectOption(merchantAccountsSelect, `${a.accountNumber} - ${a.bankName}`, a.id);
            });
        })
        .catch(console.error);

    // Populate services
    ServiceAPI.getAllServices()
        .then((services) => {
            services.forEach((s) => {
                addSelectOption(merchantServicesSelect, s.name, s.id);
            });
        })
        .catch(console.error);
}

async function handleMerchantFormSubmit(e) {
    e.preventDefault();

    const method = merchantForm.dataset.method || "POST";
    const body = {
        businessName: merchantBusinessNameInput.value.trim(),
        gstNumber: merchantGstNumberInput.value.trim(),
        contactNumber: merchantContactNumberInput.value.trim(),
        successUrl: merchantSuccessUrlInput.value.trim(),
        failureUrl: merchantFailureUrlInput.value.trim(),
        thankYouUrl: merchantThankYouUrlInput.value.trim(),
        redirectUrl: merchantRedirectUrlInput.value.trim(),
        callbackUrl: merchantCallbackUrlInput.value.trim(),
        ipWhitelist: merchantIpWhitelistInput.value.trim(),
        accounts: Array.from(merchantAccountsSelect.selectedOptions).map((opt) => opt.value),
        services: Array.from(merchantServicesSelect.selectedOptions).map((opt) => opt.value),
    };

    try {
        setFormFieldsDisabled(merchantForm);

        if (method === "POST") {
            await MerchantAPI.createMerchant(body);
            Toast.success(Message.MERCHANT.CREATED);
            merchantForm.reset();
        } else if (method === "PUT") {
            await MerchantAPI.updateMerchant(merchantForm.dataset.editKey, body);
            Toast.success(Message.MERCHANT.UPDATED);
        }

        merchantModalInstance.hide();
        merchantDatatable.ajax.reload(null, false);
    } catch (e) {
        console.error(e);
    } finally {
        setFormFieldsDisabled(merchantForm, false);
    }
}

async function initializeEditMerchantModal(merchantId) {
    try {
        const merchant = await MerchantAPI.getMerchantById(merchantId);

        initializeMerchantModal();
        merchantForm.dataset.method = "PUT";
        merchantForm.dataset.editKey = merchant.id;

        merchantBusinessNameInput.value = merchant.businessName || "";
        merchantGstNumberInput.value = merchant.gstNumber || "";
        merchantContactNumberInput.value = merchant.contactNumber || "";
        merchantSuccessUrlInput.value = merchant.successUrl || "";
        merchantFailureUrlInput.value = merchant.failureUrl || "";
        merchantThankYouUrlInput.value = merchant.thankYouUrl || "";
        merchantRedirectUrlInput.value = merchant.redirectUrl || "";
        merchantCallbackUrlInput.value = merchant.callbackUrl || "";
        merchantIpWhitelistInput.value = merchant.ipWhitelist || "";

        // Preselect assigned accounts and services
        setTimeout(() => {
            Array.from(merchantAccountsSelect.options).forEach((opt) => {
                if (merchant.accounts.includes(opt.value)) opt.selected = true;
            });
            Array.from(merchantServicesSelect.options).forEach((opt) => {
                if (merchant.services.includes(opt.value)) opt.selected = true;
            });
        }, 300);

        merchantModalInstance.show();
    } catch (e) {
        console.error(e);
    }
}

async function handleDeleteMerchant(uuid) {
    const confirmed = confirm(Message.MERCHANT.DELETE_CONFIRM);
    if (!confirmed) return;

    try {
        await MerchantAPI.deleteMerchant(uuid);
        Toast.success(Message.MERCHANT.DELETED);
        if (merchantDatatable) merchantDatatable.ajax.reload(null, false);
    } catch (e) {
        console.error(e);
    }
}

export {
    addMerchantBtn,
    initializeMerchantModal,
    merchantForm,
    handleMerchantFormSubmit,
    initializeMerchantTable,
};