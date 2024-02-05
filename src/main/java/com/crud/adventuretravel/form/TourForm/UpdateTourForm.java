package com.crud.adventuretravel.form.TourForm;

import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.TourDto;
import com.crud.adventuretravel.form.ErrorNotification;
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

public class UpdateTourForm extends FormLayout {

    private TourView tourView;
    private TextField country = new TextField("Country");
    private TextArea description = new TextArea("Description");
    private DatePicker startDate = new DatePicker("Start Date");
    private DatePicker endDate = new DatePicker("End Date");
    private TextField startLocation = new TextField("Start Location");
    private TextField endLocation = new TextField("End Location");
    private NumberField priceEuro = new NumberField("Price EURO");
    private NumberField pricePln = new NumberField("Price PLN");
    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");
    private Binder<TourDto> binder = new Binder<TourDto>(TourDto.class);
    private TourService service = TourService.getInstance();
    private ErrorNotification errorNotification = new ErrorNotification();

    public UpdateTourForm(TourView tourView) {

        HorizontalLayout buttons = new HorizontalLayout(update, delete, cancel);
        setButtons();
        add(country, description, startDate, endDate, startLocation, endLocation, priceEuro, pricePln, buttons);
        binder.bindInstanceFields(this);
        this.tourView = tourView;
    }

    private void setButtons() {

        update.addClickListener(event -> {
            try {
                update();
            } catch (BackendRequestException e) {
                errorNotification.showNotification("Tour doesn't exist!");
            }
        });
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        update.addClickShortcut(Key.ENTER);

        delete.addClickListener(event -> delete());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);
    }

    private void update() throws BackendRequestException {

        TourDto tourDto = binder.getBean();
        service.updateTourDto(tourDto);
        tourView.refresh();
        setTourDto(null);
    }

    private void delete() {

        TourDto tourDto = binder.getBean();
        service.deleteTourDto(tourDto);
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
}
