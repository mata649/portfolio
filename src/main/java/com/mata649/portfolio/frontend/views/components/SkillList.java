package com.mata649.portfolio.frontend.views.components;

import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.mata649.portfolio.skill.service.SkillService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class SkillList extends HorizontalLayout {

    public SkillList(List<SkillResponse> skills) {

        skills.forEach(skill -> {
            Button button = createSkillButton(skill);
            HorizontalLayout container = new HorizontalLayout();
            container.add(button);
            container.setWidthFull();
            container.setJustifyContentMode(JustifyContentMode.CENTER);
            add(container);
        });
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidthFull();
        addClassNames(LumoUtility.Gap.Column.XLARGE, LumoUtility.Display.GRID,
                LumoUtility.Grid.Breakpoint.Medium.COLUMNS_3, LumoUtility.Margin.Top.XLARGE);
    }

    private Button createSkillButton(SkillResponse skill) {
        Button button = new Button(skill.getName());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button.addClassNames(LumoUtility.FontSize.XXLARGE);

        button.setMaxWidth("16rem");
        button.addClickListener(event -> {
            fireEvent(new ToggleSelectedSkill(this, skill.getId(), button));

        });
        return button;
    }

    @Getter
    public class ToggleSelectedSkill extends ComponentEvent<SkillList> {
        private final UUID id;
        private final Button button;

        public ToggleSelectedSkill(SkillList source, UUID id, Button button) {
            super(source, false);
            this.id = id;
            this.button = button;
        }


    }
}
