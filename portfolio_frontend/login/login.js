import {API_HOST} from "../main.js";
import axios from "axios";

async function redirectIfAuthenticated() {
    try {
        const token = localStorage.getItem("token");
        if (token == null) {
            return;
        }

        const resp = await axios.post(API_HOST + "/api/auth/validate", null, {
            headers: {
                "Authorization": `Bearer ${token}`,
            }
        });
        if (resp.status == 200) {
            window.location.href = "/admin";
        }
    } catch (error) {
        console.log(error);
    }
}

redirectIfAuthenticated();

async function login(email, password) {
    try {
        const resp = await axios.post(API_HOST + "/api/auth/login", {email, password});
        if (resp.status === 200) {
            localStorage.setItem("token", resp.data.token);
            window.location.href = "/admin";
        }
    } catch (err) {
        if (err.response.status === 401) {
            setErrorMessage("Incorrect email or password");
        } else {
            setErrorMessage("Unexpected error");
        }
    }
}

function setErrorMessage(message) {
    window.dispatchEvent(new CustomEvent("set-error-message", {
        detail: {errorMessage: message}
    }));
}

window.login = login;