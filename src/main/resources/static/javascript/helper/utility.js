function getLoader() {
    const span = document.createElement("span");
    span.className = "spinner-border";
    span.ariaHidden = true;
    return span;
}

function createAnchor(href, className, target = "_self") {
    return createElement("a", className, [
        ["href", href],
        ["target", target],
    ]);
}

function setFormFieldsDisabled(form, disable = true) {
    if (!(form instanceof HTMLFormElement)) {
        console.error("Expected a form element");
        return;
    }

    // Disable/enable all form fields
    const fields = form.querySelectorAll("input, select, textarea, fieldset");
    fields.forEach((field) => {
        if (field.disabled !== disable) {
            field.disabled = disable;
        }
    });

    // Handle the single submit button (either inside or outside)
    let submitButton = form.querySelector('button[type="submit"]');
    if (!submitButton) {
        const formId = form.id;
        if (formId) {
            submitButton = document.querySelector(`button[type="submit"][form="${formId}"]`);
        }
    }

    if (submitButton) {
        submitButton.disabled = disable;
        setButtonLoading(submitButton, disable);
    }
}

function setupDefaultOption(selectElement, text) {
    const option = addSelectOption(selectElement, text, "");
    option.selected = true;
    option.disabled = true;
    return option;
};

function addSelectOption(selectElement, text, value) {
    const option = new Option(text, value);
    selectElement.add(option);
    return option;
}

function scrollToPosition(position) {
    const isTop = position.toLowerCase() === "top";
    const scrollHeight = isTop ? 0 : document.documentElement.scrollHeight;
    document.body.scrollTop = scrollHeight;
    document.documentElement.scrollTop = scrollHeight;
}

function setButtonLoading(button, isLoading = true) {
    if (!(button instanceof HTMLButtonElement)) {
        console.error("Expected a button element");
        return;
    }

    const isAlreadyLoading = button.dataset.loading === "true";

    if (isLoading) {
        if (isAlreadyLoading) return;

        // Save original content (safely)
        const originalNodes = Array.from(button.childNodes);
        button.dataset.loading = "true";
        button._originalNodes = originalNodes;

        // Clear button content (no innerHTML)
        while (button.firstChild) {
            button.removeChild(button.firstChild);
        }

        // Create and append spinner
        const spinner = document.createElement("span");
        spinner.className = "spinner-border spinner-border-sm";
        spinner.setAttribute("role", "status");
        spinner.setAttribute("aria-hidden", "true");

        // Create loading text
        const text = document.createElement("span");
        text.className = "ms-1"; // margin-left (Bootstrap spacing)
        text.textContent = "Loading...";

        button.appendChild(spinner);
        button.appendChild(text);

        button.disabled = true;
    } else {
        if (!isAlreadyLoading) return;

        // Clear current (spinner) content
        while (button.firstChild) {
            button.removeChild(button.firstChild);
        }

        // Restore saved nodes
        if (button._originalNodes) {
            button._originalNodes.forEach((node) => button.appendChild(node.cloneNode(true)));
            delete button._originalNodes;
        }

        button.disabled = false;
        delete button.dataset.loading;
    }
}

function toggleLoading(element, show = true) {
    if (!element) return;

    const loaderId = `loader_overlay_${element.id}`;

    if (show) {
        // Prevent duplicate overlay
        if (document.getElementById(loaderId)) return;

        // Ensure the parent is position-relative
        element.classList.add("position-relative");

        // Disable interaction and dim
        element.classList.add("pe-none", "opacity-50");

        // Create overlay container
        const overlay = document.createElement("div");
        overlay.id = loaderId;
        overlay.classList.add("position-absolute", "top-0", "start-0", "w-100", "h-100", "d-flex", "justify-content-center", "align-items-center", "bg-white", "bg-opacity-75");

        // Create spinner
        const spinner = document.createElement("div");
        spinner.classList.add("spinner-border", "text-primary");
        spinner.setAttribute("role", "status");

        // Create visually-hidden text
        const srText = document.createElement("span");
        srText.classList.add("visually-hidden");
        srText.textContent = "Loading...";

        // Nest elements
        spinner.appendChild(srText);
        overlay.appendChild(spinner);
        element.appendChild(overlay);
    } else {
        const existingOverlay = document.getElementById(loaderId);
        if (existingOverlay) {
            existingOverlay.remove();
        }

        // Re-enable interaction and reset appearance
        element.classList.remove("pe-none", "opacity-50");
    }
}

const DATATABLE_DEFAULT_OPTIONS = {
    ajax: {
        dataSrc: "",
        type: "GET",
    },
    processing: true,
    searching: true,
    pageLength: 10,
    ordering: true,
    order: [[0, "asc"]],
    lengthChange: true,
    emptyTable: "No data available in table",
    info: true,
    bStateSave: false,
    language: {
        search: "",
        searchPlaceholder: "Search...",
        lengthMenu: "_MENU_",
        /*emptyTable: "No blog",
        info: "Showing _START_ to _END_ of _TOTAL_ blog",
        infoEmpty: "Showing 0 to 0 of 0 blog",
        infoFiltered: "(filtered from _MAX_ total blog)",
        zeroRecords: "No record found",
        paginate: {
            next: '<i class="fa-solid fa-arrow-right"></i>',
            previous: '<i class="fa-solid fa-arrow-left"></i>',
        },*/
    },
};

function getIcon(className) {
    const icon = document.createElement("i");
    icon.className = `${className} cursor-pointer`;
    return icon;
}

const getEyeSlashIcon = () => getIcon("fa-solid fa-eye-slash");
const getEyeIcon = () => getIcon("fa-solid fa-eye");
const getTrashIcon = () => getIcon("fa-solid fa-trash");
const getPenIcon = () => getIcon("fa-solid fa-pen");
const getSendIcon = () => getIcon("fa-solid fa-paper-plane");
const getPdfIcon = () => getIcon("fa-solid fa-file-pdf");
const getExcelIcon = () => getIcon("fa-solid fa-file-excel");

function formatDateTime(isoDate, locale = "en-US") {
    if (!isoDate) return "-";

    const date = new Date(isoDate);
    return date.toLocaleString(locale, {
        year: "numeric",
        month: "short",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false
    });
}

export {
    getLoader,
    createAnchor,
    setFormFieldsDisabled,
    addSelectOption,
    setupDefaultOption,
    scrollToPosition,
    setButtonLoading,
    toggleLoading,
    DATATABLE_DEFAULT_OPTIONS,
    getEyeSlashIcon,
    getEyeIcon,
    getTrashIcon,
    getPenIcon,
    getSendIcon,
    getPdfIcon,
    getExcelIcon,
    formatDateTime
};
