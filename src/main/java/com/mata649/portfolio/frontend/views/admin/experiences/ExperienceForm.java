package com.mata649.portfolio.frontend.views.admin.experiences;

import com.mata649.portfolio.experience.dtos.ExperienceResponse;
import com.mata649.portfolio.experience.dtos.SaveExperienceRequest;
import com.mata649.portfolio.frontend.views.admin.projects.ProjectForm;
import com.mata649.portfolio.project.dtos.ProjectResponse;
import com.mata649.portfolio.project.dtos.SaveProjectRequest;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExperienceForm extends FormLayout {
    TextField position = new TextField("Position");
    TextArea company = new TextArea("Company");
    TextField location = new TextField("Location");
    TextArea description = new TextArea("Description");

    DatePicker startTime = new DatePicker("Start Time");
    DatePicker endTime = new DatePicker("End Time");

    Checkbox currentJob = new Checkbox("Current Job");

    CheckboxGroup<UUID> skills = new CheckboxGroup<>("Skills");

    Binder<SaveExperienceRequest> binder = new BeanValidationBinder<>(SaveExperienceRequest.class);
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private ExperienceResponse selectedProject;

    public ExperienceForm(List<SkillResponse> skills) {
        binder.bindInstanceFields(this);
        configureSkillsGroup(skills);
        clearForm();

        configureCurrentJob();
        add(position,
                company,
                location,
                description,
                startTime,
                endTime,
                currentJob,
                this.skills, createButtonsLayout());
    }

    private void configureCurrentJob() {
    currentJob.addClickListener(event -> toggleEndTime());
    }

    private void toggleEndTime() {
        endTime.setVisible(!currentJob.getValue());
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> fireEvent(new ExperienceForm.Delete(this, selectedProject)));

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(event -> {
            clearForm();
            fireEvent(new ExperienceForm.Cancel(this));
        });

        layout.add(save, delete, cancel);
        return layout;
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            if (selectedProject == null) {
                fireEvent(new ExperienceForm.Save(this, this.binder.getBean()));
            } else {
                fireEvent(new ExperienceForm.Update(this, this.binder.getBean(), this.selectedProject.getId()));
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

    public void setExperience(ExperienceResponse experienceResponse) {
        if (experienceResponse == null) {
            binder.setBean(SaveExperienceRequest
                    .builder()
                    .startTime(LocalDate.now())
                    .endTime(LocalDate.now())
                    .build());
            clearForm();
        } else {
            binder.setBean(SaveExperienceRequest.builder()
                    .position(experienceResponse.getPosition())
                    .company(experienceResponse.getCompany())
                    .location(experienceResponse.getLocation())
                    .description(experienceResponse.getDescription())
                    .startTime(experienceResponse.getStartTime())
                    .endTime(experienceResponse.getEndTime())
                    .currentJob(experienceResponse.getCurrentJob())
                    .skills(experienceResponse
                            .getSkills()
                            .stream()
                            .map(SkillResponse::getId).collect(Collectors.toSet()))
                    .build());
            this.selectedProject = experienceResponse;
            save.setText("Edit");
            delete.setEnabled(true);
        }
        toggleEndTime();
    }

    public void clearForm() {
        this.binder.setBean(SaveExperienceRequest
                .builder()
                .startTime(LocalDate.now())
                .endTime(LocalDate.now())
                .build());
        this.selectedProject = null;
        this.delete.setEnabled(false);
        this.save.setText("Save");
    }


    @Getter
    class Save extends ComponentEvent<ExperienceForm> {

        private final SaveExperienceRequest request;

        public Save(ExperienceForm source, SaveExperienceRequest request) {
            super(source, false);
            this.request = request;
        }
    }

    @Getter
    class Update extends ComponentEvent<ExperienceForm> {

        private final SaveExperienceRequest request;
        private final UUID id;

        public Update(ExperienceForm source, SaveExperienceRequest request, UUID id) {
            super(source, false);
            this.request = request;
            this.id = id;
        }
    }

    @Getter
    class Delete extends ComponentEvent<ExperienceForm> {

        private final ExperienceResponse request;

        public Delete(ExperienceForm source, ExperienceResponse experienceResponse) {
            super(source, false);
            this.request = experienceResponse;
        }

    }

    @Getter
    class Cancel extends ComponentEvent<ExperienceForm> {
        public Cancel(ExperienceForm source) {
            super(source, false);
        }
    }
}
