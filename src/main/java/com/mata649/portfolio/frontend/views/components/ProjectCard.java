package com.mata649.portfolio.frontend.views.components;

import com.mata649.portfolio.project.dtos.ProjectResponse;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ProjectCard extends VerticalLayout {
    private final ProjectResponse project;

    public ProjectCard(ProjectResponse projectResponse) {
        project = projectResponse;

        addClassNames(LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_10,
                LumoUtility.BorderRadius.MEDIUM);
        setWidth("18rem");
        setHeight("36rem");
        setAlignItems(Alignment.CENTER);
        H3 title = new H3(projectResponse.getName());
        VerticalLayout internalCardLayout = getInternalCardLayout();
        add(
                title,
                internalCardLayout
        );
    }

    private VerticalLayout getInternalCardLayout() {
        VerticalLayout internalCardLayout = new VerticalLayout();
        internalCardLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        internalCardLayout.setAlignItems(Alignment.CENTER);
        internalCardLayout.setPadding(false);
        internalCardLayout.setHeightFull();
        Scroller descriptionScroller = getDescriptionScroller();
        VerticalLayout bottomLayout = getBottomLayout();
        internalCardLayout.add(descriptionScroller, bottomLayout);
        return internalCardLayout;
    }

    private VerticalLayout getBottomLayout() {
        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setAlignItems(Alignment.CENTER);
        Anchor githubLink = new Anchor(project.getGithubLink(), "See in GitHub");
        githubLink.setTarget("_blank");
        HorizontalLayout skillsLayout = getSkillsLayout();
        bottomLayout.add(githubLink, skillsLayout);
        return bottomLayout;
    }

    private Scroller getDescriptionScroller() {
        Paragraph description = new Paragraph(project.getDescription());
        description.addClassName(LumoUtility.FontSize.LARGE);
        Scroller descriptionScroller = new Scroller(description);
        descriptionScroller.setHeight("20rem");
        return descriptionScroller;
    }

    private HorizontalLayout getSkillsLayout() {
        var skillsLayout = new HorizontalLayout();
        project.getSkills().forEach(skillResponse -> {
            skillsLayout.add(new Text(skillResponse.getName()));
        });
    return skillsLayout;
    }
}
