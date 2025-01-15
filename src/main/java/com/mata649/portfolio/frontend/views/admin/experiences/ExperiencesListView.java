package com.mata649.portfolio.frontend.views.admin.experiences;

import com.mata649.portfolio.experience.dtos.ExperienceResponse;
import com.mata649.portfolio.experience.service.ExperienceService;
import com.mata649.portfolio.frontend.views.admin.AdminLayout;
import com.mata649.portfolio.frontend.views.shared.ConfirmationDialog;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.mata649.portfolio.skill.service.SkillService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "/experiences", layout = AdminLayout.class)
@PermitAll
@PageTitle("Experiences | Mata649")
public class ExperiencesListView extends VerticalLayout {
    Grid<ExperienceResponse> grid = new Grid<>(ExperienceResponse.class);
    ExperienceForm form;
    ConfirmationDialog deleteDialog = new ConfirmationDialog();
    Dialog formDialog = new Dialog();
    Button add = new Button("Add");

    private final ExperienceService experienceService;

    public ExperiencesListView(SkillService skillService, ExperienceService experienceService) {

        this.experienceService = experienceService;

        form = new ExperienceForm(skillService.findAll());
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        configureGrid();
        configureEvents();
        configureDeleteDialog();
        configureAddButton();
        formDialog.add(form);
        formDialog.addDialogCloseActionListener(event -> closeForm());
        H1 title = new H1("Experiences");
        add(title, grid, add);
    }

    private void configureAddButton() {
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addClickListener(event -> formDialog.open());
    }

    private void configureDeleteDialog() {
        deleteDialog.confirm.setText("Delete");
        deleteDialog.confirm.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteDialog.cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }

    private void configureEvents() {
        ComponentUtil.addListener(form, ExperienceForm.Save.class, this::saveExperience);
        ComponentUtil.addListener(form, ExperienceForm.Update.class, this::updateExperience);
        ComponentUtil.addListener(form, ExperienceForm.Delete.class, this::deleteExperience);
        ComponentUtil.addListener(form, ExperienceForm.Cancel.class, this::closeForm);
    }

    private void closeForm(ExperienceForm.Cancel cancel) {
        form.setExperience(null);
        grid.asSingleSelect().clear();
        formDialog.close();
    }

    private void closeForm() {
        form.setExperience(null);
        grid.asSingleSelect().clear();
        formDialog.close();
        deleteDialog.close();
    }

    private void deleteExperience(ExperienceForm.Delete event) {

        deleteDialog.setHeaderTitle(String.format("Do you want to delete %s  - %s?",
                event.getRequest().getPosition(),
                event.getRequest().getCompany()));
        deleteDialog.setConfirmationEvent(() -> {
            try {
                experienceService.delete(event.getRequest().getId());
                updateList();
            } catch (Exception e) {
                Notification.show("Unexpected Error happened!");
            }
            closeForm();
        });
        deleteDialog.open();


    }

    private void updateExperience(ExperienceForm.Update event) {
        try {
            experienceService.update(event.getId(), event.getRequest());
            updateList();
        } catch (Exception e) {
            Notification.show("Unexpected Internal");
        }
        closeForm();
    }

    private void saveExperience(ExperienceForm.Save event) {
        try {
            experienceService.create(event.getRequest());
            updateList();
            form.clearForm();
            formDialog.close();
        } catch (Exception e) {
            Notification.show("Unexpected Internal");
        }
    }


    private void configureGrid() {
        grid.setColumns("position", "company", "location", "startTime" );
        grid.addColumn(experience ->
                experience.
                        getCurrentJob() ? "" : experience.getEndTime())
                .setHeader("End Time");
        grid.addColumn(experience ->
                        experience
                                .getSkills()
                                .stream()
                                .map(SkillResponse::getName)
                                .collect(Collectors.joining(" - ")))
                .setHeader("Skills");

        grid.setWidthFull();
        grid.asSingleSelect().addValueChangeListener(event -> {
            form.setExperience(event.getValue());
            formDialog.open();
        });
        updateList();

    }

    private void updateList() {
        List<ExperienceResponse> experiencesPage = experienceService.findAll();
        grid.setItems(experiencesPage.stream().toList());
    }
}
