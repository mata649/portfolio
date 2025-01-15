package com.mata649.portfolio.frontend.views.admin.skills;

import com.mata649.portfolio.skill.dtos.SaveSkillRequest;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;

import java.util.UUID;

public class SkillForm extends FormLayout {
    TextField name = new TextField("Name");
    Binder<SaveSkillRequest> binder = new BeanValidationBinder<>(SaveSkillRequest.class);
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    private SkillResponse selectedSkill;

    public SkillForm() {
        binder.bindInstanceFields(this);
        clearForm();
        add(name,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> fireEvent(new Delete(this, selectedSkill)));

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(event -> clearForm());
        layout.add(save, delete, cancel);
        return layout;
    }

    public void clearForm() {
        this.binder.setBean(new SaveSkillRequest());
        this.selectedSkill = null;
        this.delete.setEnabled(false);
        this.save.setText("Save");
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            if (selectedSkill == null) {
                fireEvent(new Save(this, this.binder.getBean()));
            } else {
                fireEvent(new Update(this, this.binder.getBean(), this.selectedSkill.getId()));

            }
        }
    }

    public void setSkill(SkillResponse skillResponse) {
        if (skillResponse == null) {
            binder.setBean(new SaveSkillRequest());
            clearForm();
        } else {
            binder.setBean(new SaveSkillRequest(skillResponse.getName()));
            this.selectedSkill = skillResponse;
            save.setText("Edit");
            delete.setEnabled(true);
        }
    }

    @Getter
    class Save extends ComponentEvent<SkillForm> {

        private final SaveSkillRequest request;

        public Save(SkillForm source, SaveSkillRequest request) {
            super(source, false);
            this.request = request;
        }
    }

    @Getter
    class Update extends ComponentEvent<SkillForm> {

        private final SaveSkillRequest request;
        private final UUID id;

        public Update(SkillForm source, SaveSkillRequest request, UUID id) {
            super(source, false);
            this.request = request;
            this.id = id;
        }
    }

    @Getter
    class Delete extends ComponentEvent<SkillForm> {

        private final SkillResponse request;

        public Delete(SkillForm source, SkillResponse skillResponse) {
            super(source, false);
            this.request = skillResponse;
        }

    }
}
