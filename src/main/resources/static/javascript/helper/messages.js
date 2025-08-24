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
    USER: {
        ADMIN_CREATED: "Admin user created successfully",
        CREATED: "User '{name}' created successfully",
        DELETE_CONFIRM: "Are you sure you want to delete user '{name}' with ID '{id}'?",
        UNVERIFIED_EMAIL_SENT: "Verification email sent successfully",
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
