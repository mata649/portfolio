import {validateToken} from "../main.js";
import {addSkill, cleanSkillsState, deleteSkill, editSkill, getSkills, setSkillToEdit} from "../src/skills.js";
import {
    addExperience,
    cleanExperiencesState,
    deleteExperience,
    editExperience,
    getDateYearMonthFormat,
    getExperiences,
    setExperienceToEdit
} from "../src/experiences.js";

import {
    addProject,
    cleanProjectsState,
    deleteProject,
    editProject,
    getProjects,
    setProjectToEdit
} from "../src/projects.js";

validateToken();

const token = localStorage.getItem("token");

window.getDateYearMonthFormat = getDateYearMonthFormat;
window.setExperienceToEdit = setExperienceToEdit;
window.addExperience = addExperience;
window.getExperiences = getExperiences;
window.editExperience = editExperience;
window.deleteExperience = deleteExperience;
window.cleanExperiencesState = cleanExperiencesState;

window.setSkillToEdit = setSkillToEdit;
window.addSkill = addSkill;
window.getSkills = getSkills;
window.editSkill = editSkill;
window.deleteSkill = deleteSkill;
window.cleanSkillsState = cleanSkillsState;


window.setProjectToEdit = setProjectToEdit;
window.addProject = addProject;
window.getProjects = getProjects;
window.editProject = editProject;
window.deleteProject = deleteProject;
window.cleanProjectsState = cleanProjectsState;

