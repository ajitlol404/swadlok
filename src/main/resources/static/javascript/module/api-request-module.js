import { deleteRequest, getRequest, patchRequest, postRequest, putRequest, uploadFileRequest } from "../helper/fetch-helper.js";

const BASE_URLS = {
    FILE: "/api/v1/files",
    SETUP: "/api/v1/setup",
    SIGN_UP: "/api/v1/signup",
    UNVERIFIED_USER: "/api/v1/unverified-users",
    PROFILE: "/api/v1/profile",
    SECRET_KEY: "/api/v1/secret-key",
    VERIFY_ACCOUNT: "/api/v1/verify-account",
    FORGOT_PASSWORD: "/api/v1/forgot-password",
    FORGOT_PASSWORD_LINK: "/api/v1/forgot-password-link",
    FORGOT_PASSWORD_KEY: "/api/v1/forgot-password-key",
    SMTP: "/api/v1/admin/smtp",
    ENUMERATION_VALUES: "/api/v1/enumeration-values",
    CATEGORY: "/api/v1/admin/categories",
    CATEGORIES_DATATABLE: "/api/v1/admin/categories/datatable",
};

const FileAPI = {
    upload: (imageCategory, file) => uploadFileRequest(BASE_URLS.FILE, imageCategory, file),
};

const SetupAPI = {
    createAdmin: (body) => postRequest(`${BASE_URLS.SETUP}/users`, body),
    uploadImage: (imageCategory, file) => uploadFileRequest(`${BASE_URLS.SETUP}/upload`, imageCategory, file),
};

const UnverifiedUserAPI = {
    createUnverifiedUser: (body) => postRequest(BASE_URLS.UNVERIFIED_USER, body),
    getUnverifiedUserById: (id) => getRequest(`${BASE_URLS.UNVERIFIED_USER}/${id}/verify`),
    verifyAndSaveUser: (id, body) => postRequest(`${BASE_URLS.UNVERIFIED_USER}/${id}/verify`, body),
};

const SmtpAPI = {
    createSmtpConfig: (body) => postRequest(BASE_URLS.SMTP, body),
    getAllSmtpConfigs: () => getRequest(BASE_URLS.SMTP),
    getSmtpById: (uuid) => getRequest(`${BASE_URLS.SMTP}/${uuid}`),
    updateSmtpById: (uuid, body) => putRequest(`${BASE_URLS.SMTP}/${uuid}`, body),
    toggleSmtpStatus: (uuid, body) => patchRequest(`${BASE_URLS.SMTP}/${uuid}/toggle`, body),
    deleteSmtpById: (uuid) => deleteRequest(`${BASE_URLS.SMTP}/${uuid}`),
};

const ProfileAPI = {
    getProfile: () => getRequest(BASE_URLS.PROFILE),
    updateProfile: (body) => patchRequest(BASE_URLS.PROFILE, body),
};

export { BASE_URLS, FileAPI, SetupAPI, UnverifiedUserAPI, SmtpAPI, ProfileAPI };
