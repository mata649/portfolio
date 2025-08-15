import axios from "axios";
import {API_HOST} from "./config.js";
import {uuidv7} from "uuidv7";

export function cleanProjectsState(data) {
    data.form.name = "";
    data.form.description = "";
    data.form.skills = [];
    data.form.githubLink = "";
    data.selectedProject = {};
    data.editMode = false;
}

export async function getProjects() {
    try {
        const resp = await axios.get(API_HOST + "/api/projects");

        if (resp.status === 200) {
            return resp.data;
        }
        return [];
    } catch (err) {
        console.log(err);
        return [];
    }
}

export async function addProject(data) {
    try {
        const newProject = {
            id: uuidv7(),
            name: data.form.name,
            description: data.form.description,
            githubLink: data.form.githubLink,
            skills: data.form.skills
        };
        const resp = await axios.post(API_HOST + "/api/projects", newProject, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        if (resp.status === 201) {
            data.projects = [...data.projects, newProject];
            cleanProjectsState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export async function editProject(data) {
    try {
        const newProject = {
            name: data.form.name,
            description: data.form.description,
            githubLink: data.form.githubLink,
            skills: data.form.skills
        };

        const resp = await axios.put(API_HOST + `/api/projects/${data.selectedProject.id}`, newProject, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        if (resp.status === 200) {
            data.projects = data.projects.map(s => {
                if (s.id === data.selectedProject.id) {
                    return {id: s.id, ...newProject};
                }
                return s;
            });
            cleanProjectsState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export async function deleteProject(data) {
    try {
        const resp = await axios.delete(API_HOST + `/api/projects/${data.selectedProject.id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (resp.status === 200) {
            data.projects = data.projects.filter(s => s.id !== data.selectedProject.id);
            cleanProjectsState(data);
        }
    } catch (err) {
        console.log(err);
    }
}

export function setProjectToEdit(project, data) {
    data.form.name = project.name;
    data.form.description = project.description;
    data.form.githubLink = project.githubLink;
    data.form.skills = project.skills.map(s => s.id);
    data.selectedProject = project;
    data.editMode = true;

}

