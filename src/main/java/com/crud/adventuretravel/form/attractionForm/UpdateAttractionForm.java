package com.crud.adventuretravel.form.attractionForm;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.AttractionDto;
import com.crud.adventuretravel.form.Notifications;
import com.crud.adventuretravel.service.AttractionService;
import com.crud.adventuretravel.service.ReferentialIntegrityViolationException;
import com.crud.adventuretravel.view.AttractionView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;

public class UpdateAttractionForm extends FormLayout {

    private AttractionView attractionView;
    private AttractionService attractionService = AttractionService.getInstance();
    private Notifications notifications = new Notifications();
    private boolean isAdmin;

    private TextField name = new TextField("Name");
    private TextField city = new TextField("City");
    private TextArea description = new TextArea("Description");
    private NumberField priceEuro = new NumberField("Price EURO");

    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Button deactivate = new Button("Deactivate");
    private Button cancel = new Button("Cancel");
    private Binder<AttractionDto> binder = new Binder<>(AttractionDto.class);


    public UpdateAttractionForm(AttractionView attractionView) {

        setAuthenticationContext();
        createFormLayout();
        setButtons();
        this.attractionView = attractionView;
    }

    private void setAuthenticationContext() {

        AuthenticationContext authContext = new AuthenticationContext();
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        });
    }

    private void createFormLayout() {

        city.setRequiredIndicatorVisible(true);
        city.setMinWidth("380px");
        description.setRequiredIndicatorVisible(true);
        description.setMinWidth("380px");
        priceEuro.setRequiredIndicatorVisible(true);
        priceEuro.setMinWidth("380px");

        VerticalLayout verticalLayout = new VerticalLayout(city, description, priceEuro,
                new HorizontalLayout(update, delete, deactivate, cancel));
        verticalLayout.setSizeFull();
        add(verticalLayout);
        binder.bindInstanceFields(this);
    }

    private void setButtons() {

        update.addClickListener(event -> {
            try {
                update();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            }
        });
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        update.addClickShortcut(Key.ENTER);

        if (!isAdmin) delete.setVisible(false);
        delete.addClickListener(event -> {
            try {
                delete();
            } catch (BackendRequestException | ReferentialIntegrityViolationException e) {
                notifications.showNotification(e.getMessage());
            }
        });
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        if (!isAdmin) deactivate.setVisible(false);
        deactivate.addClickListener(event -> {
            try {
                deactivate();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            }
        });
        deactivate.addThemeVariants(ButtonVariant.LUMO_ERROR);

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);
    }

    private void update() throws BackendRequestException {

        AttractionDto attractionDto = binder.getBean();
        attractionService.updateAttractionDto(attractionDto);
        attractionView.refresh();
        setAttractionDto(null);
    }

    private void delete() throws BackendRequestException, ReferentialIntegrityViolationException {

        AttractionDto attractionDto = binder.getBean();
        attractionService.deleteAttractionDto(attractionDto);
        attractionView.refresh();
        setAttractionDto(null);
    }

    private void deactivate() throws BackendRequestException {

        AttractionDto attractionDto = binder.getBean();
        attractionService.updateAttractionDtoDeactivate(attractionDto);
        attractionView.refresh();
        setAttractionDto(null);
    }

    private void cancel() {

        attractionView.refresh();
        setAttractionDto(null);
    }

    public void setAttractionDto(AttractionDto attractionDto) {

        binder.setBean(attractionDto);
        if (attractionDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            city.focus();
        }
    }
}
