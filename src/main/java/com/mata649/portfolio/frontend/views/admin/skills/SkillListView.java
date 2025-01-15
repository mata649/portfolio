package com.mata649.portfolio.frontend.views.admin.skills;

import com.mata649.portfolio.frontend.views.admin.AdminLayout;
import com.mata649.portfolio.frontend.views.shared.ConfirmationDialog;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.mata649.portfolio.skill.service.SkillService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection.Breakpoint;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route(value = "/skills", layout = AdminLayout.class)
@PermitAll
@PageTitle("Skills | Mata649")
public class SkillListView extends VerticalLayout {
    Grid<SkillResponse> grid = new Grid<>(SkillResponse.class);
    SkillForm form = new SkillForm();
    ConfirmationDialog deleteDialog = new ConfirmationDialog();
    private final SkillService skillService;

    public SkillListView(SkillService skillService) {
        this.skillService = skillService;
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        configureGrid();
        configureEvents();
        configureDeleteDialog();
        VerticalLayout layout = createLayout();
        H1 title = new H1("Skills");
        add(title,layout);
    }

    private void configureDeleteDialog() {
        deleteDialog.confirm.setText("Delete");
        deleteDialog.confirm.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteDialog.cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }

    private void configureEvents() {
        ComponentUtil.addListener(form, SkillForm.Save.class, this::saveSkill);
        ComponentUtil.addListener(form, SkillForm.Update.class, this::updateSkill);
        ComponentUtil.addListener(form, SkillForm.Delete.class, this::deleteSkill);
    }

    private void deleteSkill(SkillForm.Delete event) {

        deleteDialog.setHeaderTitle(String.format("Do you want to delete %s ?", event.getRequest().getName()));
        deleteDialog.setConfirmationEvent(() -> {
            try {
                skillService.delete(event.getRequest().getId());
                updateList();
                form.clearForm();
            } catch (Exception e) {
                Notification.show("Unexpected Error happened!");
            }
                deleteDialog.close();
        });
        deleteDialog.open();


    }

    private void updateSkill(SkillForm.Update event) {
        try {
            skillService.update(event.getRequest(), event.getId());
            updateList();
            form.clearForm();
        } catch (Exception e) {
            Notification.show("Unexpected Internal");
        }
    }

    private void saveSkill(SkillForm.Save event) {
        try {
            skillService.create(event.getRequest());
            updateList();
            form.clearForm();
        } catch (Exception e) {
            Notification.show("Unexpected Internal");
        }
    }

    private VerticalLayout createLayout() {
        VerticalLayout layout = new VerticalLayout(grid, form);
        layout.addClassName(Breakpoint.Small.ROW);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }

    private void configureGrid() {
        grid.setColumns("name");
        grid.setWidthFull();
        grid.asSingleSelect().addValueChangeListener(event -> form.setSkill(event.getValue()));
        updateList();

    }

    private void updateList() {
        List<SkillResponse> skills = skillService.findAll();
        grid.setItems(skills);
    }
}
