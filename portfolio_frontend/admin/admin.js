import {API_HOST, validateToken} from "../main.js";
import axios from "axios";
import {uuidv7} from "uuidv7";

validateToken();
const token = localStorage.getItem("token");

function cleanSkillsState(data) {
    data.nameText = "";
    data.selectedSkill = {};
    data.editMode = false;

}

async function getSkills() {
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

async function addSkill(data) {
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

async function editSkill(data) {
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

async function deleteSkill(data) {
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

function setSkillToEdit(skill, data) {
    data.nameText = skill.name;
    data.selectedSkill = skill;
    data.editMode = true;

}

window.setSkillToEdit = setSkillToEdit;
window.addSkill = addSkill;
window.getSkills = getSkills;
window.editSkill = editSkill;
window.deleteSkill = deleteSkill;
window.cleanSkillsState = cleanSkillsState;


function cleanProjectsState(data) {
    data.form.name = "";
    data.form.description = "";
    data.form.skills = [];
    data.form.githubLink = "";
    data.selectedProject = {};
    data.editMode = false;
}

async function getProjects() {
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

async function addProject(data) {
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

async function editProject(data) {
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

async function deleteProject(data) {
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

function setProjectToEdit(project, data) {
    data.form.name = project.name;
    data.form.description = project.description;
    data.form.githubLink = project.githubLink;
    data.form.skills = project.skills.map(s => s.id);
    data.selectedProject = project;
    data.editMode = true;

}


window.setProjectToEdit = setProjectToEdit;
window.addProject = addProject;
window.getProjects = getProjects;
window.editProject = editProject;
window.deleteProject = deleteProject;
window.cleanProjectsState = cleanProjectsState;

function getDateYearMonthFormat(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    return `${year}-${month}`;
}

function cleanExperiencesState(data) {
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

async function getExperiences() {
    try {
        const resp = await axios.get(API_HOST + "/api/experiences");
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

async function addExperience(data) {
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

async function editExperience(data) {
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

async function deleteExperience(data) {
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

function setExperienceToEdit(experience, data) {
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

window.getDateYearMonthFormat = getDateYearMonthFormat;
window.setExperienceToEdit = setExperienceToEdit;
window.addExperience = addExperience;
window.getExperiences = getExperiences;
window.editExperience = editExperience;
window.deleteExperience = deleteExperience;
window.cleanExperiencesState = cleanExperiencesState;