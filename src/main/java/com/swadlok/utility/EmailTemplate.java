package com.swadlok.utility;

import lombok.Getter;

import static com.swadlok.utility.AppConstant.BRAND_NAME;

@Getter
public enum EmailTemplate {

    UNVERIFIED_USER_REGISTRATION_TEMPLATE(
            "Complete Your Registration - " + BRAND_NAME,
            """
                    <html>
                        <body>
                            <div style="text-align: center;">
                                <p>Hi <b>%s</b>, Welcome to <b>%s</b></p>
                                <p>We are thrilled to have you on board. To complete your registration, please set your password by clicking the button below:</p>
                                <p><a style="font-size: 16px;padding: 10px 20px;background-color: #007BFF;color: white;text-decoration: none;border-radius: 5px;" class="button" href="%s">Set Your Password</a></p>
                                <p>This link will expire in %s minutes, so please set your password as soon as possible.</p>
                                <p>
                                    Regards, <br />
                                    <b>%s</b>
                                </p>
                            </div>
                        </body>
                    </html>
                    """),

    ACCOUNT_CREATED_SUCCESS_TEMPLATE(
            "Account Created Successfully - " + BRAND_NAME,
            """
                    <html>
                        <body>
                            <div style="text-align: center;">
                                <p>Hi <b>%s</b>,</p>
                                <p>Welcome to <b>%s</b>!</p>
                                <p>Your account has been successfully created. You can now log in using the following credentials:</p>
                                <p><b>Email:</b> %s</p>
                                <p><b>Password:</b> Set at the time of account creation.</p>
                                <p>If you have any questions, feel free to contact us.</p>
                                <p>
                                    Regards, <br />
                                    <b>%s</b>
                                </p>
                            </div>
                        </body>
                    </html>
                    """);

    private final String subject;
    private final String template;

    EmailTemplate(String subject, String template) {
        this.subject = subject;
        this.template = template;
    }

    public String formatBody(Object... args) {
        return template.formatted(args);
    }

}