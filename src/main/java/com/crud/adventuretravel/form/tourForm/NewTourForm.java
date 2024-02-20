package com.crud.adventuretravel.form.tourForm;


import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.TourDto;
import com.crud.adventuretravel.form.Notifications;
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

import java.time.LocalDate;

public class NewTourForm extends FormLayout {

    private TourView tourView;
    private TourService service = TourService.getInstance();
    private Notifications notifications = new Notifications();

    private TextField name = new TextField("Name");
    private TextField country = new TextField("Country");
    private TextArea description = new TextArea("Description");
    private DatePicker startDate = new DatePicker("Start Date");
    private DatePicker endDate = new DatePicker("End Date");
    private TextField startLocation = new TextField("Start Location");
    private TextField endLocation = new TextField("End Location");
    private NumberField priceEuro = new NumberField("Price EURO");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Binder<TourDto> binder = new Binder<>(TourDto.class);


    public NewTourForm(TourView tourView) {

        createFormLayout();
        setButtons();
        this.tourView = tourView;
    }

    private void createFormLayout() {

        name.setRequiredIndicatorVisible(true);
        country.setRequiredIndicatorVisible(true);
        description.setRequiredIndicatorVisible(true);
        startDate.setRequiredIndicatorVisible(true);
        endDate.setRequiredIndicatorVisible(true);
        startLocation.setRequiredIndicatorVisible(true);
        endLocation.setRequiredIndicatorVisible(true);
        priceEuro.setRequiredIndicatorVisible(true);

        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));
        setColspan(description, 2);

        add(name, country, description, startDate, endDate, startLocation, endLocation, priceEuro,
                new HorizontalLayout(save, cancel));
        binder.bindInstanceFields(this);
    }

    private void setButtons() {

        save.addClickListener(event -> {
            try {
                save();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            } catch (NullPointerException e) {
                notifications.showNotification("Please fill in all fields marked with a dot.");
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);
    }

    private void save() throws BackendRequestException {

        TourDto tourDto = binder.getBean();
        if (isNotNull(tourDto)) {
            service.saveTourDto(tourDto);
            tourView.refresh();
            setTourDto(null);
        } else {
            notifications.showNotification("Please fill in all fields marked with a dot.");
        }
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
            name.focus();
        }
    }

    private boolean isNotNull(TourDto tourDto) {

        String name = tourDto.getName();
        String country = tourDto.getCountry();
        String description = tourDto.getDescription();
        LocalDate startDate = tourDto.getStartDate();
        LocalDate endDate = tourDto.getEndDate();
        String startLocation = tourDto.getStartLocation();
        String endLocation = tourDto.getEndLocation();
        double priceEuro = tourDto.getPriceEuro();
        return name != null && country != null && description != null && startDate != null && endDate != null
                && startLocation != null && endLocation != null && priceEuro != 0;
    }
}
