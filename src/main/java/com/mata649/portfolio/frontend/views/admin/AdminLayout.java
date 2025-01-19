package com.mata649.portfolio.frontend.views.admin;

import com.mata649.portfolio.frontend.views.admin.experiences.ExperiencesListView;
import com.mata649.portfolio.frontend.views.admin.projects.ProjectsListView;
import com.mata649.portfolio.frontend.views.admin.skills.SkillListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class AdminLayout extends AppLayout {
    private final transient AuthenticationContext authContext;
    List<RouterLink> links = List.of(
            new RouterLink("Skills", SkillListView.class),
            new RouterLink("Projects", ProjectsListView.class),
            new RouterLink("Experiences", ExperiencesListView.class)
    );

    public AdminLayout(AuthenticationContext authContext) {
        this.authContext = authContext;
        H1 logo = new H1("Portfolio Administration");
        HorizontalLayout linksContainer = getLinksContainer();

        HorizontalLayout
                header =
                authContext.getAuthenticatedUser(UserDetails.class)
                        .map(user -> {
                            Button logout = new Button("Logout", click ->
                                    this.authContext.logout());
                            logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                            linksContainer.add(logout);
                            return new HorizontalLayout(logo, linksContainer);
                        }).orElseGet(() -> new HorizontalLayout(logo));
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        addToNavbar(header);
    }

    private HorizontalLayout getLinksContainer() {
        HorizontalLayout linksContainer = new HorizontalLayout();

        links.forEach(routerLink -> {
            routerLink.addClassName(FontSize.LARGE);
            linksContainer.add(routerLink);
        });

        linksContainer.setAlignItems(FlexComponent.Alignment.CENTER);

        return linksContainer;
    }

}
