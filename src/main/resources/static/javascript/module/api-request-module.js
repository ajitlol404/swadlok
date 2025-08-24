import { deleteRequest, getRequest, patchRequest, postRequest, putRequest, uploadFileRequest } from "../helper/fetch-helper.js";

const BASE_URLS = {
    FILE: "/api/v1/files",
    SETUP: "/setup",
};

const FileAPI = {
    upload: (imageCategory, file) => uploadFileRequest(BASE_URLS.FILE, imageCategory, file),
};

const SetupAPI = {
    createAdmin: (body) => postRequest(`${BASE_URLS.SETUP}/users`, body),
    uploadImage: (imageCategory, file) => uploadFileRequest(`${BASE_URLS.SETUP}/upload`, imageCategory, file),
};


export { BASE_URLS, FileAPI, SetupAPI };
