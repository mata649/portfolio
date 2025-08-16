import "./style.css";
import Alpine from "alpinejs";
import axios from "axios";
import {API_HOST} from "./src/config";
import {getSkills} from "./src/skills";
import {getProjects} from "./src/projects";
import {getExperiences} from "./src/experiences";

window.Alpine = Alpine;

Alpine.start();

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

async function getAboutMeText() {
    const resp = await axios.get("https://gist.githubusercontent.com/mata649/f1f90149cc08469f2bcecbe0e94c4aec/raw/2f32f05257b0dbede57cbc32c2d1dd541b88fd85/about_me.txt");
    if (resp.status !== 200) {
        return "";
    }
    return resp.data;
}

function formatDate(date) {
    const options = {
        year: "numeric",
        month: "long"
    };
    const d = new Date(date);
    const formattedDate = new Intl.DateTimeFormat("en-US", options).format(d);
    return formattedDate;
}

window.getAboutMeText = getAboutMeText;
window.getSkills = getSkills;
window.getProjects = getProjects;
window.getExperiences = getExperiences;
window.formatDate = formatDate;