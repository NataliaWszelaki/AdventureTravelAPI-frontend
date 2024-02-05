package com.crud.adventuretravel.form.AttractionForm;

import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.AttractionDto;
import com.crud.adventuretravel.form.ErrorNotification;
import com.crud.adventuretravel.service.AttractionService;
import com.crud.adventuretravel.view.AttractionView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class UpdateAttractionForm extends FormLayout {

    private AttractionView attractionView;
    private TextField name = new TextField("Name");
    private TextField city = new TextField("City");
    private TextArea description = new TextArea("Description");
    private NumberField priceEuro = new NumberField("Price EURO");
    private NumberField pricePln = new NumberField("Price PLN");

    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

    private Binder<AttractionDto> binder = new Binder<AttractionDto>(AttractionDto.class);
    private AttractionService attractionService = AttractionService.getInstance();
    private ErrorNotification errorNotification = new ErrorNotification();

    public UpdateAttractionForm(AttractionView attractionView) {

        HorizontalLayout buttons = new HorizontalLayout(update, delete, cancel);
        setButtons();
        add(city, description, priceEuro, pricePln, buttons);
        binder.bindInstanceFields(this);
        this.attractionView = attractionView;
    }

    public void setButtons() {

        update.addClickListener(event -> {
            try {
                update();
            } catch (BackendRequestException e) {
                errorNotification.showNotification("Attraction doesn't exist!");
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
        AttractionDto attractionDto = binder.getBean();
        attractionService.updateAttractionDto(attractionDto);
        attractionView.refresh();
        setAttractionDto(null);
    }

    private void delete() {
        AttractionDto attractionDto = binder.getBean();
        attractionService.deleteAttractionDto(attractionDto);
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
