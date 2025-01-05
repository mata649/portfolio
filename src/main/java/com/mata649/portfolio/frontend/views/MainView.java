package com.mata649.portfolio.frontend.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route
@AnonymousAllowed
public class MainView extends VerticalLayout {
    public MainView() {
        add(new Text("Hello World"));
    }
}
