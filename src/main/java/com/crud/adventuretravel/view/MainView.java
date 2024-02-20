package com.crud.adventuretravel.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Main")
@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    public MainView() {
        setSpacing(false);

        H2 header = new H2("Adventure travel CRM");

        Button button = new Button("Discover Your Adventure!");
        button.addThemeVariants(ButtonVariant.LUMO_LARGE);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        StreamResource imageResource = new StreamResource("travel.png",
                () -> getClass().getResourceAsStream("/images/travel.png"));
        Image img = new Image(imageResource, "travel");
        img.setWidth("600px");
        add(header, button, img);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}
