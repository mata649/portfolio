package com.mata649.portfolio.frontend.views.admin.projects;

import com.mata649.portfolio.project.dtos.ProjectResponse;
import com.mata649.portfolio.project.dtos.SaveProjectRequest;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProjectForm extends FormLayout {
    TextField name = new TextField("Name");
    TextArea description = new TextArea("Description");
    TextField githubLink = new TextField("GitHub Link");

    CheckboxGroup<UUID> skills = new CheckboxGroup<>("Skills");

    Binder<SaveProjectRequest> binder = new BeanValidationBinder<>(SaveProjectRequest.class);
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private ProjectResponse selectedProject;

    public ProjectForm(List<SkillResponse> skills) {
        binder.bindInstanceFields(this);
        configureSkillsGroup(skills);
        clearForm();
        add(name, description, githubLink, this.skills, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> fireEvent(new Delete(this, selectedProject)));

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(event -> {
            clearForm();
            fireEvent(new Cancel(this));
        });

        layout.add(save, delete, cancel);
        return layout;
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            if (selectedProject == null) {
                fireEvent(new Save(this, this.binder.getBean()));
            } else {
                fireEvent(new Update(this, this.binder.getBean(), this.selectedProject.getId()));
            }
        }
    }

    private void configureSkillsGroup(List<SkillResponse> skills) {
        this.skills.setItems(skills.stream().map(SkillResponse::getId).collect(Collectors.toSet()));
        this.skills.setItemLabelGenerator(item -> skills
                .stream()
                .filter(skill -> skill
                        .getId()
                        .equals(item))
                .findFirst()
                .get()
                .getName());
    }

    public void setProject(ProjectResponse projectResponse) {
        if (projectResponse == null) {
            binder.setBean(SaveProjectRequest.builder()
                    .build());
            clearForm();
        } else {
            binder.setBean(SaveProjectRequest.builder()
                    .name(projectResponse.getName())
                    .description(projectResponse.getDescription())
                    .githubLink(projectResponse.getGithubLink())
                    .skills(projectResponse
                            .getSkills()
                            .stream()
                            .map(SkillResponse::getId).collect(Collectors.toSet()))
                    .build());
            this.selectedProject = projectResponse;
            save.setText("Edit");
            delete.setEnabled(true);
        }
    }

    public void clearForm() {
        this.binder.setBean(new SaveProjectRequest());
        this.selectedProject = null;
        this.delete.setEnabled(false);
        this.save.setText("Save");
    }


    @Getter
    class Save extends ComponentEvent<ProjectForm> {

        private final SaveProjectRequest request;

        public Save(ProjectForm source, SaveProjectRequest request) {
            super(source, false);
            this.request = request;
        }
    }

    @Getter
    class Update extends ComponentEvent<ProjectForm> {

        private final SaveProjectRequest request;
        private final UUID id;

        public Update(ProjectForm source, SaveProjectRequest request, UUID id) {
            super(source, false);
            this.request = request;
            this.id = id;
        }
    }

    @Getter
    class Delete extends ComponentEvent<ProjectForm> {

        private final ProjectResponse request;

        public Delete(ProjectForm source, ProjectResponse projectResponse) {
            super(source, false);
            this.request = projectResponse;
        }

    }

    @Getter
    class Cancel extends ComponentEvent<ProjectForm> {
        public Cancel(ProjectForm source) {
            super(source, false);
        }
    }
}
