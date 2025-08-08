import "./style.css";
import Alpine from "alpinejs";
import axios from "axios";

window.Alpine = Alpine;

Alpine.start();

const API_HOST = import.meta.env.VITE_API_HOST;

async function validateToken() {
    try {
        const token = localStorage.getItem("token");
        if (token === null) {
            window.location.href = "/";
        }

        const resp = await axios.post(API_HOST + "/api/auth/validate", null, {
            headers: {
                "Authorization": `Bearer ${token}`,
            }
        });
        if (resp.status !== 200) {
            window.location.href = "/";
        }
    } catch (error) {
        window.location.href = "/";
    }
}

export {
    API_HOST,
    validateToken
};