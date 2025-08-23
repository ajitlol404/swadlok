import { deleteRequest, getRequest, patchRequest, postRequest, putRequest, uploadFileRequest } from "../helper/fetch-helper.js";

const BASE_URLS = {
    SERVICE: "/api/v1/super-admin/services",
    CURRENCY: "/api/v1/super-admin/currencies",
    BANK: "/api/v1/super-admin/banks",
    ADMIN: "/api/v1/super-admin/admins",
    METHOD: "/api/v1/super-admin/methods",
    PROFILE: "/api/v1/profile",
};

const ServiceAPI = {
    createService: (body) => postRequest(BASE_URLS.SERVICE, body),
    getAllServices: () => getRequest(BASE_URLS.SERVICE),
    getServiceById: (id) => getRequest(`${BASE_URLS.SERVICE}/${id}`),
    updateService: (id, body) => putRequest(`${BASE_URLS.SERVICE}/${id}`, body),
    deleteService: (id) => deleteRequest(`${BASE_URLS.SERVICE}/${id}`),
};

const CurrencyAPI = {
    createCurrency: (body) => postRequest(BASE_URLS.CURRENCY, body),
    getAllCurrencies: () => getRequest(BASE_URLS.CURRENCY),
    getCurrencyById: (id) => getRequest(`${BASE_URLS.CURRENCY}/${id}`),
    updateCurrency: (id, body) => putRequest(`${BASE_URLS.CURRENCY}/${id}`, body),
    deleteCurrency: (id) => deleteRequest(`${BASE_URLS.CURRENCY}/${id}`),
};

const BankAPI = {
    createBank: (body) => postRequest(BASE_URLS.BANK, body),
    getAllBanks: () => getRequest(BASE_URLS.BANK),
    getBankById: (id) => getRequest(`${BASE_URLS.BANK}/${id}`),
    updateBank: (id, body) => putRequest(`${BASE_URLS.BANK}/${id}`, body),
    deleteBank: (id) => deleteRequest(`${BASE_URLS.BANK}/${id}`),
};

const AdminAPI = {
    createAdmin: (body) => postRequest(BASE_URLS.ADMIN, body),
    getAllAdmins: () => getRequest(BASE_URLS.ADMIN),
    getAdminById: (id) => getRequest(`${BASE_URLS.ADMIN}/${id}`),
    updateAdmin: (id, body) => putRequest(`${BASE_URLS.ADMIN}/${id}`, body),
    deleteAdmin: (id) => deleteRequest(`${BASE_URLS.ADMIN}/${id}`),
    toggleAdminActive: (id) => patchRequest(`${BASE_URLS.ADMIN}/${id}/toggle-active`)
};

const MethodAPI = {
    createMethod: (body) => postRequest(BASE_URLS.METHOD, body),
    getAllMethods: () => getRequest(BASE_URLS.METHOD),
    getMethodsByService: (serviceId) => getRequest(`${BASE_URLS.METHOD}/service/${serviceId}`),
    getMethodById: (id) => getRequest(`${BASE_URLS.METHOD}/${id}`),
    updateMethod: (id, body) => putRequest(`${BASE_URLS.METHOD}/${id}`, body),
    deleteMethod: (id) => deleteRequest(`${BASE_URLS.METHOD}/${id}`),
};

const ProfileAPI = {
    getProfile: () => getRequest(BASE_URLS.PROFILE),
    updateProfile: (body) => patchRequest(BASE_URLS.PROFILE, body),
};

export { BASE_URLS, ServiceAPI, CurrencyAPI, BankAPI, AdminAPI, MethodAPI, ProfileAPI };
