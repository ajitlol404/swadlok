import { setFormFieldsDisabled, formatDateTime, DATATABLE_DEFAULT_OPTIONS, getPenIcon, getTrashIcon, setupDefaultOption, addSelectOption } from "../../helper/utility.js";
import { BASE_URLS, AccountAPI, BankAPI, CommonAPI } from "../../module/api-request-module.js";
import Toast from "../../helper/toast.js";
import { Message } from "../../helper/messages.js";

const accountModal = document.getElementById("account_modal");
const accountModalInstance = bootstrap.Modal.getOrCreateInstance(accountModal);
const addAccountBtn = document.getElementById("add_account_btn");
const accountForm = document.getElementById("account_form");
const accountNumberInput = document.getElementById("account_number");
const accountBankSelect = document.getElementById("account_bank");
const accountFormSubmitBtn = document.getElementById("account_form_submit_btn");

let accountDatatable;

function initializeAccountTable() {
    if (accountDatatable) return accountDatatable;

    accountDatatable = new DataTable("#account_table", {
        ...DATATABLE_DEFAULT_OPTIONS,
        language: {
            ...DATATABLE_DEFAULT_OPTIONS.language,
            searchPlaceholder: "Search by code, account number, or bank...",
        },
        serverSide: true,
        ajax: {
            ...DATATABLE_DEFAULT_OPTIONS.ajax,
            url: `${BASE_URLS.ACCOUNT}/datatable`,
            dataSrc: "data",
            data: function (d) {
                return d; // let DataTables send params unchanged
            },
            error: function (xhr) {
                Toast.danger("Failed to load accounts");
                if (xhr.status >= 400 && xhr.status < 600) {
                    const response = xhr.responseJSON;
                    const message = response ? response.message : "An error occurred";
                    Toast.danger(message);
                }
            },
        },
        order: [[4, "desc"]], // updatedAt
        columnDefs: [
            {
                defaultContent: "-",
                targets: "_all",
            },
            { targets: 0, data: "code" },
            { targets: 1, data: "accountNumber" },
            { targets: 2, data: "bankName" },
            { targets: 3, data: "createdByUsername" },
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
                    editSpan.addEventListener("click", () => initializeEditAccountModal(rowData.id));

                    const deleteSpan = document.createElement("span");
                    deleteSpan.className = "text-danger cursor-pointer";
                    deleteSpan.appendChild(getTrashIcon());
                    deleteSpan.addEventListener("click", () => handleDeleteAccount(rowData.id));

                    td.replaceChildren(editSpan, deleteSpan);
                },
            },
        ],
    });

    return accountDatatable;
}

function initializeAccountModal() {
    accountForm.reset();
    accountForm.dataset.method = "POST";
    delete accountForm.dataset.editKey;

    accountBankSelect.length = 0;
    setupDefaultOption(accountBankSelect, "Select bank");

    CommonAPI.getAllBanks()
        .then((banks) => {
            banks.forEach((b) => {
                addSelectOption(accountBankSelect, `${b.name} (${b.currency.symbol})`, b.id);
            });
        })
        .catch((e) => {
            console.error(e);
        });
}

async function handleAccountFormSubmit(e) {
    e.preventDefault();

    const method = accountForm.dataset.method || "POST";
    const body = {
        accountNumber: accountNumberInput.value.trim(),
        bankId: accountBankSelect.value,
    };

    try {
        setFormFieldsDisabled(accountForm);

        if (method === "POST") {
            await AccountAPI.createAccount(body);
            Toast.success(Message.ACCOUNT.CREATED);
            accountForm.reset();
        } else if (method === "PUT") {
            await AccountAPI.updateAccount(accountForm.dataset.editKey, body);
            Toast.success(Message.ACCOUNT.UPDATED);
        }

        accountModalInstance.hide();
        accountDatatable.ajax.reload(null, false);
    } catch (e) {
        console.log(e);
    } finally {
        setFormFieldsDisabled(accountForm, false);
    }
}

async function initializeEditAccountModal(accountId) {
    try {
        const { id, accountNumber, bankName, bankId } = await AccountAPI.getAccountById(accountId);

        initializeAccountModal();
        accountForm.dataset.method = "PUT";
        accountForm.dataset.editKey = id;
        accountNumberInput.value = accountNumber;
        accountBankSelect.value = bankId;

        accountModalInstance.show();
    } catch (e) {
        console.log(e);
    }
}

async function handleDeleteAccount(uuid) {
    const confirmed = confirm(Message.ACCOUNT.DELETE_CONFIRM);
    if (!confirmed) return;

    try {
        await AccountAPI.deleteAccount(uuid);
        Toast.success(Message.ACCOUNT.DELETED);
        if (accountDatatable) accountDatatable.ajax.reload(null, false);
    } catch (e) {
        console.log(e);
    }
}

export { addAccountBtn, initializeAccountModal, accountForm, handleAccountFormSubmit, initializeAccountTable };
