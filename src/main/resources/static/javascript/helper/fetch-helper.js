import Toast from "../helper/toast.js";

const csrfHeaderMetaKey = document.querySelector("meta[name=_csrf_header]").content;
const csrfHeaderMetaValue = document.querySelector("meta[name=_csrf]").content;

async function fetchRequest(url, method, requestBody = null) {
    const headers = {
        Accept: "application/json",
        "Content-Type": "application/json",
        [csrfHeaderMetaKey]: csrfHeaderMetaValue,
    };

    const options = {
        method,
        headers,
    };

    if (requestBody) {
        options.body = JSON.stringify(requestBody);
    }

    try {
        const response = await fetch(url, options);
        let body = await response.text();

        if (response.headers.get("Content-Type")?.includes("json")) {
            body = JSON.parse(body);
        }

        if (!response.ok) {
            const error = {
                status: response.status,
                body,
            };
            handleFetchError(error);
            return Promise.reject(error);
        }

        return body;
    } catch (networkError) {
        // Handle network failure (e.g., no internet, timeout)
        const error = {
            status: 0,
            body: { message: "Network error or server unreachable." },
        };
        handleFetchError(error);
        return Promise.reject(error);
    }
}

async function getRequest(url) {
    return fetchRequest(url, "GET");
}

async function postRequest(url, requestBody) {
    return fetchRequest(url, "POST", requestBody);
}

async function patchRequest(url, requestBody) {
    return fetchRequest(url, "PATCH", requestBody);
}

async function putRequest(url, requestBody) {
    return fetchRequest(url, "PUT", requestBody);
}

async function deleteRequest(url) {
    return fetchRequest(url, "DELETE");
}

async function uploadFileRequest(url, imageCategory, file) {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("imageCategory", imageCategory);

    try {
        const response = await fetch(url, {
            method: "POST",
            body: formData,
            headers: {
                [csrfHeaderMetaKey]: csrfHeaderMetaValue,
            },
        });

        let body = await response.text();
        if (response.headers.get("Content-Type")?.includes("json")) {
            body = JSON.parse(body);
        }

        if (!response.ok) {
            const error = {
                status: response.status,
                body,
            };
            handleFetchError(error);
            return Promise.reject(error);
        }

        return body;
    } catch (networkError) {
        const error = {
            status: 0,
            body: { message: "Network error or server unreachable." },
        };
        handleFetchError(error);
        return Promise.reject(error);
    }
}

function handleFetchError(e) {
    let errorMessage = "Something went wrong";

    if (e.body.message) {
        errorMessage = e.body.message;
    }
    if (e.body.detail) {
        errorMessage = e.body.detail;
    }

    if (e.status === 400) {
        Toast.danger(errorMessage);
    } else if (e.status === 404) {
        Toast.warning(errorMessage || "Not found");
    } else if (e.status === 403) {
        Toast.warning(errorMessage || "Access denied");
    } else {
        Toast.danger(errorMessage);
    }
}

export { getRequest, postRequest, putRequest, patchRequest, deleteRequest, handleFetchError, uploadFileRequest };
