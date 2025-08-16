import axios from "axios";
import {API_HOST} from "./config.js";
import {uuidv7} from "uuidv7";
const token = localStorage.getItem("token");
export function cleanSkillsState(data) {
    data.nameText = "";
    data.selectedSkill = {};
    data.editMode = false;

}

export async function getSkills() {
    try {
        const resp = await axios.get(API_HOST + "/api/skills");

        if (resp.status === 200) {
            return resp.data;
        }
        return [];
    } catch (err) {
        console.log(err);
        return [];
    }
}

export async function addSkill(data) {
    try {
        const newSkill = {
            id: uuidv7(),
            name: data.nameText,
        };
        const resp = await axios.post(API_HOST + "/api/skills", newSkill, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        if (resp.status === 201) {
            data.skills = [...data.skills, newSkill];
            cleanSkillsState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export async function editSkill(data) {
    try {
        const newSkill = {
            name: data.nameText,
        };
        const resp = await axios.put(API_HOST + `/api/skills/${data.selectedSkill.id}`, newSkill, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        if (resp.status === 200) {
            data.skills = data.skills.map(s => {
                if (s.id === data.selectedSkill.id) {
                    return {id: s.id, ...newSkill};
                }
                return s;
            });
            cleanSkillsState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export async function deleteSkill(data) {
    try {
        const resp = await axios.delete(API_HOST + `/api/skills/${data.selectedSkill.id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (resp.status === 200) {
            data.skills = data.skills.filter(s => s.id !== data.selectedSkill.id);
            cleanSkillsState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export function setSkillToEdit(skill, data) {
    data.nameText = skill.name;
    data.selectedSkill = skill;
    data.editMode = true;

}
