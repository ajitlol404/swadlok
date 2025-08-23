const COLORS = {
    success: "bg-success text-white",
    warning: "bg-warning text-dark",
    danger: "bg-danger text-white",
    info: "bg-info text-dark",
};

const ICONS = {
    success: "fa-solid fa-circle-check",
    warning: "fa-solid fa-triangle-exclamation",
    danger: "fa-solid fa-circle-xmark",
    info: "fa-solid fa-circle-info",
};

function createToast(message, type = "info") {
    // Remove any existing toast container
    const oldContainer = document.querySelector(".toast-container");
    if (oldContainer) {
        oldContainer.remove();
    }

    // Create new toast container
    const container = document.createElement("div");
    container.className = "toast-container position-fixed top-0 start-50 translate-middle-x p-3";
    document.body.appendChild(container);

    // Create toast element
    const toastEl = document.createElement("div");
    toastEl.className = `toast align-items-center ${COLORS[type] || COLORS.info}`;
    toastEl.setAttribute("role", "alert");
    toastEl.setAttribute("aria-live", "assertive");
    toastEl.setAttribute("aria-atomic", "true");
    toastEl.dataset.bsDelay = "5000";

    // Create toast body
    const toastBody = document.createElement("div");
    toastBody.className = "toast-body text-center";

    // Create icon span
    const icon = document.createElement("i");
    icon.className = ICONS[type] || ICONS.info;
    icon.setAttribute("aria-hidden", "true");

    // Add icon and message
    toastBody.appendChild(icon);
    toastBody.appendChild(document.createTextNode("\u00A0"));
    toastBody.appendChild(document.createTextNode(message));

    toastEl.appendChild(toastBody);
    container.appendChild(toastEl);

    // Show toast using Bootstrap's API
    const toastInstance = bootstrap.Toast.getOrCreateInstance(toastEl);
    toastInstance.show();

    // Remove the container after toast disappears
    toastEl.addEventListener("hidden.bs.toast", () => container.remove());
}

const Toast = {
    success: (msg) => createToast(msg, "success"),
    warning: (msg) => createToast(msg, "warning"),
    danger: (msg) => createToast(msg, "danger"),
    info: (msg) => createToast(msg, "info"),
};

// Exported Toast API
export default Toast;
