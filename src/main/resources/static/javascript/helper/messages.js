/*
// Static
Toast.success(Messages.SMTP.CREATED);

// Dynamic (manual format)
Toast.success(formatMessage(Messages.SMTP.DELETE_CONFIRM, { uuid: "abc123" }));

// Dynamic (auto fetch + format by key path)
Toast.success(getMessage("USER.DELETE_CONFIRM", { name: "Alice", id: "u1" }));
*/

// Message templates
const Message = {
    SERVICE: {
        CREATED: "Service saved successfully",
        DELETED: "Service deleted successfully",
        UPDATED: "Service updated successfully",
        AJAX_ERR: "Service datatables ajax error",
        DELETE_CONFIRM: "Are you sure you want to delete this service?",
    },
    CURRENCY: {
        CREATED: "Currency created successfully",
        UPDATED: "Currency updated successfully",
        DELETED: "Currency deleted successfully",
        AJAX_ERR: "Currency datatables ajax error",
        DELETE_CONFIRM: "Are you sure you want to delete this currency?",
    },
    BANK: {
        CREATED: "Bank created successfully",
        UPDATED: "Bank updated successfully",
        DELETED: "Bank deleted successfully",
        AJAX_ERR: "Bank datatables ajax error",
        DELETE_CONFIRM: "Are you sure you want to delete this bank?",
    },
    ADMIN: {
        CREATED: "Admin created successfully",
        UPDATED: "Admin updated successfully",
        DELETED: "Admin deleted successfully",
        AJAX_ERR: "Admin datatables ajax error",
        DELETE_CONFIRM: "Are you sure you want to delete this admin?",
    },
    PROFILE: {
        UPDATED: "Profile updated successfully",
        PASSWORD_CHANGED: "Password updated successfully",
        AJAX_ERR: "Profile ajax error",
        UPDATE_CONFIRM: "Are you sure you want to update your profile?",
    },
    COMMON: {
        ERROR_GENERIC: "Something went wrong",
        DELETE_CONFIRM: "Are you sure you want to delete '{item}'?",
    },
};

// Simple message formatter
function formatMessage(template, params = {}) {
    return template.replace(/{(\w+)}/g, (_, key) => params[key] ?? `{${key}}`);
}

// Optional helper to fetch & format a message by key
function getMessage(path, params = {}) {
    const keys = path.split(".");
    let template = Message;
    for (const key of keys) {
        template = template?.[key];
    }

    if (!template || typeof template !== "string") return path;
    return formatMessage(template, params);
}

export { Message, formatMessage, getMessage };
