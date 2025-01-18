package com.mata649.portfolio.frontend.views.components;

import com.mata649.portfolio.project.dtos.ProjectResponse;
import com.mata649.portfolio.project.service.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public class ProjectCardList extends VerticalLayout {

    private final ProjectService projectService;
    private Integer selectedPage = 0;
    HorizontalLayout pages = new HorizontalLayout();
    HorizontalLayout cardsContainer = new HorizontalLayout();

    public ProjectCardList(ProjectService projectService, List<UUID> selectedSkills) {

        this.projectService = projectService;
        cardsContainer.addClassNames(
                LumoUtility.Display.GRID,
                LumoUtility.Grid.Breakpoint.Large.COLUMNS_3
        );
        updateCardList(selectedSkills);
        setAlignItems(Alignment.CENTER);
        add(cardsContainer, pages);
    }

    public void updateCardList(List<UUID> selectedSkills) {
        Page<ProjectResponse> projects = projectService.findBySkills(Pageable
                .ofSize(3)
                .withPage(selectedPage), selectedSkills);
        cardsContainer.removeAll();
        projects.forEach(project -> cardsContainer.add(new ProjectCard(project)));

        pages.removeAll();
        for (int i = 0; i < projects.getTotalPages(); i++) {
            Button number = new Button(Integer.toString(i + 1));

            int selectedPage = i;
            number.addClickListener(event -> {
                this.selectedPage = selectedPage;
                updateCardList(selectedSkills);
            });
            number.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            if (i == this.selectedPage) {
                number.addClassName(LumoUtility.FontWeight.BOLD);
                number.addClassName(LumoUtility.Background.CONTRAST_10);
            }
            pages.add(number);
        }
    }

}
