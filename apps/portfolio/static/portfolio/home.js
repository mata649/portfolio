function toggleSkill(skill, data) {
    if (data.skills.includes(skill)) {
        data.skills = data.skills.filter(s => s !== skill)
    } else {
        data.skills = [...data.skills, skill]
    }
    data.page = 1
}

window.toggleSkill = toggleSkill