import axios from "axios";
import {API_HOST} from "./config.js";
import {uuidv7} from "uuidv7";
const token = localStorage.getItem("token");
export function getDateYearMonthFormat(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    return `${year}-${month}`;
}

export function cleanExperiencesState(data) {
    data.form.position = "";
    data.form.company = "";
    data.form.location = "";
    data.form.description = "";
    data.form.isCurrentJob = false;
    data.form.startDate = "";
    data.form.endDate = "";
    data.form.skills = [];
    data.selectedProject = {};
    data.editMode = false;
}

export async function getExperiences() {
    try {
        const resp = await axios.get(API_HOST + "/api/experiences?sortBy=start_date&sortOrder=desc");
        if (resp.status === 200) {
            return resp.data;
        }
        return [];
    } catch (err) {
        console.log(err);
        return [];
    }
}

const DATE_SUFFIX = "-01T00:00:00Z";

export async function addExperience(data) {
    try {
        const newExperience = {
            id: uuidv7(),
            position: data.form.position,
            company: data.form.company,
            location: data.form.location,
            isCurrentJob: true,
            startDate: data.form.startDate + DATE_SUFFIX,
            endDate: data.form.endDate + DATE_SUFFIX,
            description: data.form.description,
            skills: data.form.skills
        };
        console.log(newExperience);
        const resp = await axios.post(API_HOST + "/api/experiences", newExperience, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        if (resp.status === 201) {
            data.experiences = [...data.experiences, newExperience];
            cleanExperiencesState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export async function editExperience(data) {
    try {
        console.log(data.form.skills);
        const newExperience = {
            position: data.form.position,
            company: data.form.company,
            location: data.form.location,
            isCurrentJob: data.form.isCurrentJob,
            startDate: data.form.startDate + DATE_SUFFIX,
            endDate: data.form.endDate + DATE_SUFFIX,
            description: data.form.description,
            skills: data.form.skills
        };

        const resp = await axios.put(API_HOST + `/api/experiences/${data.selectedExperience.id}`, newExperience, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        if (resp.status === 200) {
            data.experiences = data.experiences.map(s => {
                if (s.id === data.selectedExperience.id) {
                    return {id: s.id, ...newExperience};
                }
                return s;
            });
            cleanExperiencesState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export async function deleteExperience(data) {
    try {
        const resp = await axios.delete(API_HOST + `/api/experiences/${data.selectedExperience.id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (resp.status === 200) {
            data.experiences = data.experiences.filter(s => s.id !== data.selectedExperience.id);
            cleanExperiencesState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export function setExperienceToEdit(experience, data) {
    const startDate = new Date(experience.startDate);
    const endDate = new Date(experience.endDate);
    data.form.position = experience.position;
    data.form.company = experience.company;
    data.form.location = experience.location;
    data.form.isCurrentJob = experience.isCurrentJob;
    data.form.description = experience.description;
    data.form.startDate = getDateYearMonthFormat(startDate);
    data.form.endDate = getDateYearMonthFormat(endDate);
    data.form.skills = experience.skills.map(s => s.id);
    data.selectedExperience = experience;
    data.editMode = true;
}
