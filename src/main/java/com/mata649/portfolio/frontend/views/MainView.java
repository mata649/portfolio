package com.mata649.portfolio.frontend.views;

import com.mata649.portfolio.content.service.ContentService;
import com.mata649.portfolio.experience.service.ExperienceService;
import com.mata649.portfolio.frontend.views.components.ExperienceList;
import com.mata649.portfolio.frontend.views.components.ProjectCardList;
import com.mata649.portfolio.frontend.views.components.SkillList;
import com.mata649.portfolio.project.service.ProjectService;
import com.mata649.portfolio.skill.service.SkillService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Route
@AnonymousAllowed
@PageTitle("Portfolio | Mata649")
public class MainView extends VerticalLayout {

    private final ExperienceService experienceService;
    private final ContentService contentService;
    private final ProjectCardList projectCardList;
    private final List<UUID> selectedSkills = new ArrayList<>();
    SkillList skillList;

    public MainView(
            SkillService skillService,
            ProjectService projectService,
            ExperienceService experienceService,
            ContentService contentService) {

        this.experienceService = experienceService;
        this.contentService = contentService;
        skillList = new SkillList(skillService.findAll());
        setAlignItems(Alignment.CENTER);

        projectCardList = new ProjectCardList(projectService, selectedSkills);
        VerticalLayout layout = new VerticalLayout();
        configureEvents();
        ;
        layout.add(getContactSection(),
                getTopSection(),
                skillList,
                getProjectSection(),
                new ExperienceList(experienceService.findAll()));
        add(layout);
    }

    private HorizontalLayout getContactSection() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassNames(LumoUtility.FontSize.XXLARGE);
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.END);

        Anchor linkedin = new Anchor("https://www.linkedin.com/in/jose-alberto-mata-mena-a06761198/", "Linkedin");
        linkedin.setTarget("_blank");

        Anchor github = new Anchor("https://github.com/mata649", "GitHub");
        github.setTarget("_blank");

        Anchor email = new Anchor("mailto:mata649@hotmail.com", "Email");


        layout.add(linkedin, github, email);
        return layout;
    }


    private void configureEvents() {
        ComponentUtil.addListener(skillList, SkillList.ToggleSelectedSkill.class, this::toggleSelectedSkill);
    }

    private void toggleSelectedSkill(SkillList.ToggleSelectedSkill event) {
        if (selectedSkills.stream().anyMatch(s -> s.equals(event.getId()))) {
            selectedSkills.remove(event.getId());
            event.getButton().removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            event.getButton().addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        } else {
            selectedSkills.add(event.getId());
            event.getButton().removeThemeVariants(ButtonVariant.LUMO_TERTIARY);
            event.getButton().addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        projectCardList.updateCardList(selectedSkills);
    }

    private VerticalLayout getProjectSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.CENTER);
        H2 title = new H2("Projects");
        title.addClassNames("subtitle", LumoUtility.Margin.Top.XLARGE);
        layout.add(title, projectCardList);
        return layout;
    }


    private VerticalLayout getTopSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setAlignItems(Alignment.CENTER);
        H1 name = new H1("Jose Alberto Mata");
        name.addClassNames("main-title");
        Paragraph aboutMe = new Paragraph(contentService.getAboutMe());
        aboutMe.addClassName(LumoUtility.FontSize.XXLARGE);
        layout.add(name, aboutMe);
        return layout;
    }
}
