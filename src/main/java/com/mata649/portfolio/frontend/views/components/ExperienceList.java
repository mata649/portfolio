package com.mata649.portfolio.frontend.views.components;

import com.mata649.portfolio.experience.dtos.ExperienceResponse;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExperienceList extends VerticalLayout {
    public ExperienceList(List<ExperienceResponse> experiences) {

        // The new list is to avoid problems receiving an immutable list
        List<ExperienceResponse> sortedExperiences = new ArrayList<>(experiences);

        sortedExperiences.sort(Comparator.comparing(ExperienceResponse::getStartTime).reversed());
        sortedExperiences.forEach(experience -> {
            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout timeFrame = getDateFrame(experience);
            H2 position = new H2(experience.getPosition());
            H3 location = new H3(experience.getLocation());
            Paragraph description = new Paragraph(experience.getDescription());
            description.addClassName(LumoUtility.FontSize.XLARGE);
            HorizontalLayout skillsList = getSkillsList(experience.getSkills());
            layout.add(timeFrame, position, location, description, skillsList);
            add(layout);
        });
    }

    private HorizontalLayout getSkillsList(List<SkillResponse> skills) {
        HorizontalLayout layout = new HorizontalLayout();
        skills.forEach(skillResponse -> {
            Span skill = new Span(skillResponse.getName());
            skill.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.PRIMARY);
            layout.add(skill);
        });
        return layout;
    }

    private HorizontalLayout getDateFrame(ExperienceResponse experience) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassNames(LumoUtility.FontSize.XLARGE,
                LumoUtility.TextColor.SECONDARY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        Span startTime = new Span(experience.getStartTime().format(formatter));

        layout.add(startTime, new Span("-"));
        if (experience.getCurrentJob()) {
            Span currentTime = new Span("Current");
            layout.add(currentTime);
        } else {
            Span endTime = new Span(experience.getEndTime().format(formatter));
            layout.add(endTime);
        }

        return layout;
    }

}
