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

public class NewTourForm extends FormLayout {

    private TourView tourView;
    private TextField name = new TextField("Name");
    private TextField country = new TextField("Country");
    private TextArea description = new TextArea("Description");
    private DatePicker startDate = new DatePicker("Start Date");
    private DatePicker endDate = new DatePicker("End Date");
    private TextField startLocation = new TextField("Start Location");
    private TextField endLocation = new TextField("End Location");
    private NumberField priceEuro = new NumberField("Price EURO");
    private NumberField pricePln = new NumberField("Price PLN");
    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Binder<TourDto> binder = new Binder<TourDto>(TourDto.class);
    private TourService service = TourService.getInstance();
    private ErrorNotification errorNotification = new ErrorNotification();

    public NewTourForm(TourView tourView) {

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        setButtons();
        add(name, country, description, startDate, endDate, startLocation, endLocation, priceEuro, pricePln, buttons);
        binder.bindInstanceFields(this);
        this.tourView = tourView;
    }

    private void setButtons() {

        save.addClickListener(event -> {
            try {
                save();
            } catch (BackendRequestException e) {
                errorNotification.showNotification("Tour already exists!");
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        cancel.addClickListener(event -> cancel());
        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }

    private void save() throws BackendRequestException {

        TourDto tourDto = binder.getBean();
        service.saveTourDto(tourDto);
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
            name.focus();
        }
    }
}
