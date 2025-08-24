package com.swadlok.utility;

import java.util.List;

public class AppConstant {

    // Private constructor to prevent instantiation
    private AppConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String TITLE = "title";

    public static final String BRAND_NAME = "Swadlok";
    public static final String SUPPORT_TEAM = "Swadlok Team";

    public static final int MAX_STRING_DEFAULT_SIZE = 255;
    public static final int MIN_STRING_DEFAULT_SIZE = 3;

    public static final long MAX_IMAGE_SIZE = 2L * 1024 * 1024; // 2 MB

    public static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png");
    public static final int VERIFICATION_EXPIRATION_MINUTES = 15;

    public static final int MAX_SMTP_CONFIGURATIONS = 5;
    public static final int SMTP_TIMEOUT_MS = 10000;
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_STARTTLS = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
    public static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    public static final String MAIL_SMTP_WRITETIMEOUT = "mail.smtp.writetimeout";

    public static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMERIC_CHARACTERS = "0123456789";
    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:',.<>?/~`";

    public static final String ALPHA_NUMERIC_CHARACTERS = LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + NUMERIC_CHARACTERS;
    public static final String ALL_CHARACTERS = LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + NUMERIC_CHARACTERS + SPECIAL_CHARACTERS;

    public static final String BASE_API_PATH = "/api/v1";

    public static final String ADMIN_BASE_PATH = "/admin";
    public static final String ADMIN_BASE_API_PATH = BASE_API_PATH + ADMIN_BASE_PATH;

    public static final String MANAGER_BASE_PATH = "/manager";
    public static final String MANAGER_BASE_API_PATH = BASE_API_PATH + MANAGER_BASE_PATH;

    public static final String DELIVERY_BASE_PATH = "/delivery";
    public static final String DELIVERY_BASE_API_PATH = BASE_API_PATH + DELIVERY_BASE_PATH;

    public static final String CUSTOMER_BASE_PATH = "/customer";
    public static final String CUSTOMER_BASE_API_PATH = BASE_API_PATH + CUSTOMER_BASE_PATH;

    public static final String NAME_REGEX = "^(?! )[a-zA-Z0-9 .,'\\-/]*(?<! )$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[^\s]*$";

}


