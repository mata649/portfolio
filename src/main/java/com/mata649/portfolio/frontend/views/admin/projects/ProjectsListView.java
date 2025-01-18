package com.mata649.portfolio.frontend.views.admin.projects;

import com.mata649.portfolio.frontend.views.admin.AdminLayout;
import com.mata649.portfolio.frontend.views.components.ConfirmationDialog;
import com.mata649.portfolio.project.dtos.ProjectResponse;
import com.mata649.portfolio.project.service.ProjectService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.Collectors;

@Route(value = "/projects", layout = AdminLayout.class)
@PermitAll
@PageTitle("Projects | Mata649")
public class ProjectsListView extends VerticalLayout {
    Grid<ProjectResponse> grid = new Grid<>(ProjectResponse.class);
    ProjectForm form;
    ConfirmationDialog deleteDialog = new ConfirmationDialog();
    Dialog formDialog = new Dialog();
    Button add = new Button("Add");

    private final ProjectService projectService;

    public ProjectsListView(SkillService skillService, ProjectService projectService) {

        this.projectService = projectService;

        form = new ProjectForm(skillService.findAll());
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        configureGrid();
        configureEvents();
        configureDeleteDialog();
        configureAddButton();
        formDialog.add(form);
        formDialog.addDialogCloseActionListener(event -> closeForm());
        H1 title = new H1("Projects");
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
        ComponentUtil.addListener(form, ProjectForm.Save.class, this::saveProject);
        ComponentUtil.addListener(form, ProjectForm.Update.class, this::updateProject);
        ComponentUtil.addListener(form, ProjectForm.Delete.class, this::deleteProject);
        ComponentUtil.addListener(form, ProjectForm.Cancel.class, this::closeForm);
    }

    private void closeForm(ProjectForm.Cancel cancel) {
        form.setProject(null);
        grid.asSingleSelect().clear();
        formDialog.close();
    }

    private void closeForm() {
        form.setProject(null);
        grid.asSingleSelect().clear();
        formDialog.close();
        deleteDialog.close();
    }

    private void deleteProject(ProjectForm.Delete event) {

        deleteDialog.setHeaderTitle(String.format("Do you want to delete %s ?", event.getRequest().getName()));
        deleteDialog.setConfirmationEvent(() -> {
            try {
                projectService.delete(event.getRequest().getId());
                updateList();
            } catch (Exception e) {
                Notification.show("Unexpected Error happened!");
            }
            closeForm();
        });
        deleteDialog.open();


    }

    private void updateProject(ProjectForm.Update event) {
        try {
            projectService.update(event.getId(), event.getRequest());
            updateList();
        } catch (Exception e) {
            Notification.show("Unexpected Internal");
        }
        closeForm();
    }

    private void saveProject(ProjectForm.Save event) {
        try {
            projectService.create(event.getRequest());
            updateList();
            form.clearForm();
            formDialog.close();
        } catch (Exception e) {
            Notification.show("Unexpected Internal");
        }
    }


    private void configureGrid() {
        grid.setColumns("name", "githubLink");
        grid.addColumn(projectResponse ->
                        projectResponse.getSkills().stream().map(SkillResponse::getName).collect(Collectors.joining(" - ")))
                .setHeader("Skills");
        grid.setWidthFull();
        grid.asSingleSelect().addValueChangeListener(event -> {
            form.setProject(event.getValue());
            formDialog.open();
        });
        updateList();

    }

    private void updateList() {
        Page<ProjectResponse> projectsPage = projectService.findAll(Pageable.ofSize(100));
        grid.setItems(projectsPage.stream().toList());
    }
}
