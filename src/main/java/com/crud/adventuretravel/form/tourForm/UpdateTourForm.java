package com.crud.adventuretravel.form.tourForm;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.TourDto;
import com.crud.adventuretravel.form.Notifications;
import com.crud.adventuretravel.service.ReferentialIntegrityViolationException;
import com.crud.adventuretravel.service.TourService;
import com.crud.adventuretravel.view.TourView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;

public class UpdateTourForm extends FormLayout {

    private TourView tourView;
    private TourService tourService = TourService.getInstance();
    private Notifications notifications = new Notifications();
    private boolean isAdmin;

    private TextField country = new TextField("Country");
    private TextArea description = new TextArea("Description");
    private DatePicker startDate = new DatePicker("Start Date");
    private DatePicker endDate = new DatePicker("End Date");
    private TextField startLocation = new TextField("Start Location");
    private TextField endLocation = new TextField("End Location");
    private NumberField priceEuro = new NumberField("Price EURO");

    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Button deactivate = new Button("Deactivate");
    private Button cancel = new Button("Cancel");
    private Binder<TourDto> binder = new Binder<>(TourDto.class);


    public UpdateTourForm(TourView tourView) {

        setAuthenticationContext();
        createFormLayout();
        setButtons();
        this.tourView = tourView;
    }

    public void setAuthenticationContext() {

        AuthenticationContext authContext = new AuthenticationContext();
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        });
    }

    private void createFormLayout() {

        country.setRequiredIndicatorVisible(true);
        description.setRequiredIndicatorVisible(true);
        startDate.setRequiredIndicatorVisible(true);
        endDate.setRequiredIndicatorVisible(true);
        startLocation.setRequiredIndicatorVisible(true);
        endLocation.setRequiredIndicatorVisible(true);
        priceEuro.setRequiredIndicatorVisible(true);

        add(country, description, startDate, endDate, startLocation, endLocation, priceEuro,
                new HorizontalLayout(update, delete, deactivate, cancel));
        binder.bindInstanceFields(this);
    }

    private void setButtons() {

        update.addClickListener(event -> {
            try {
                update();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            } catch (NullPointerException e) {
                notifications.showNotification("Please fill in all fields marked with a dot.");
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

        TourDto tourDto = binder.getBean();
        if (isNotNull(tourDto)) {
            tourService.updateTourDto(tourDto);
            tourView.refresh();
            setTourDto(null);
        } else {
            notifications.showNotification("Please fill in all fields marked with a dot.");
        }
    }

    private void delete() throws BackendRequestException, ReferentialIntegrityViolationException {

        TourDto tourDto = binder.getBean();
        tourService.deleteTourDto(tourDto);
        tourView.refresh();
        setTourDto(null);
    }

    private void deactivate() throws BackendRequestException {

        TourDto tourDto = binder.getBean();
        tourService.updateTourDtoDeactivate(tourDto);
        tourView.refresh();
        setTourDto(null);
    }

    private void cancel() {

        tourView.refresh();
        setTourDto(null);
    }

    public void setTourDto(TourDto tourDto) {

        binder.setBean(tourDto);
        if (tourDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            country.focus();
        }
    }

    private boolean isNotNull(TourDto tourDto) {

        String country = tourDto.getCountry();
        String description = tourDto.getDescription();
        String startLocation = tourDto.getStartLocation();
        String endLocation = tourDto.getEndLocation();
        double priceEuro = tourDto.getPriceEuro();
        return !country.equals("") && !description.equals("") && !startLocation.equals("") && !endLocation.equals("")
                && priceEuro != 0;
    }
}
