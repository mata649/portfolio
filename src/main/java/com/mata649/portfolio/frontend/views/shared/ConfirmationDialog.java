package com.mata649.portfolio.frontend.views.shared;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ConfirmationDialog extends Dialog {
    public Button confirm = new Button("Yes");
    public Button cancel = new Button("Cancel");

    public ConfirmationDialog() {
        cancel.addClickListener(event -> this.close());
        this.setHeaderTitle("Are you sure?");
        HorizontalLayout layout = new HorizontalLayout(confirm, cancel);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        add(layout);
    }

    public void setConfirmationEvent(Runnable func) {
        this.confirm.addClickListener(event -> func.run());

    }


}
