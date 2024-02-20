package com.crud.adventuretravel.form.attractionForm;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.AttractionDetailsDto;
import com.crud.adventuretravel.domain.AttractionDto;
import com.crud.adventuretravel.domain.LocationSearchDto;
import com.crud.adventuretravel.form.Notifications;
import com.crud.adventuretravel.service.AttractionService;
import com.crud.adventuretravel.service.TouristAttractionApiService;
import com.crud.adventuretravel.view.AttractionView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class NewAttractionForm extends FormLayout {

    private AttractionView attractionView;
    private AttractionDto attractionDto;
    private TextField searchField = new TextField();
    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Grid<LocationSearchDto> locationSearchDtoGrid = new Grid<>();
    private Grid<AttractionDetailsDto> attractionDetailsDtoGrid = new Grid<>();
    private TouristAttractionApiService touristAttractionApiService = TouristAttractionApiService.getInstance();
    private AttractionService attractionService = AttractionService.getInstance();
    private Notifications notifications = new Notifications();

    public NewAttractionForm(AttractionView attractionView) {

        setSearchField();
        setLocationSearchDtoGrid();
        setAttractionDetailsDtoGrid();
        setButtons();

        VerticalLayout layout = new VerticalLayout(searchField, locationSearchDtoGrid, attractionDetailsDtoGrid,
                new HorizontalLayout(save, cancel));
        add(layout);

        this.attractionView = attractionView;
    }

    private void setSearchField() {

        searchField.setPlaceholder("Enter name of the city...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.setClearButtonVisible(true);
        searchField.setMinWidth("250px");
        searchField.addValueChangeListener(e -> lengthCheck(e.getValue()));
    }

    private void setLocationSearchDtoGrid() {

        locationSearchDtoGrid.addColumn(LocationSearchDto::getName).setHeader("Name").setAutoWidth(true);
        locationSearchDtoGrid.addColumn(LocationSearchDto::getAncestor).setHeader("Region").setAutoWidth(true);
        locationSearchDtoGrid.addColumn(LocationSearchDto::getCountry).setHeader("Country").setAutoWidth(true);
        locationSearchDtoGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        locationSearchDtoGrid.setMinWidth("800px");
        locationSearchDtoGrid.setMaxHeight("250px");

        locationSearchDtoGrid.asSingleSelect().addValueChangeListener(event ->
                attractionDetailsDtoGrid.setItems(touristAttractionApiService.getAllAttractionDetails(
                        locationSearchDtoGrid.asSingleSelect().getValue().getLocation_id())));
    }

    private void setAttractionDetailsDtoGrid() {

        attractionDetailsDtoGrid.addColumn(AttractionDetailsDto::getCity).setHeader("City").setAutoWidth(true);
        attractionDetailsDtoGrid.addColumn(AttractionDetailsDto::getName).setHeader("Attraction name").setAutoWidth(true);
        attractionDetailsDtoGrid.addColumn(AttractionDetailsDto::getDescription).setHeader("Description").setWidth("200px");
        attractionDetailsDtoGrid.addColumn(AttractionDetailsDto::getCategory).setHeader("Category").setAutoWidth(true);
        attractionDetailsDtoGrid.addColumn(AttractionDetailsDto::getTitle).setHeader("Title").setAutoWidth(true);
        attractionDetailsDtoGrid.addColumn(AttractionDetailsDto::getPriceEuro).setHeader("Price EURO").setAutoWidth(true);
        attractionDetailsDtoGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        attractionDetailsDtoGrid.setMinWidth("800px");
        attractionDetailsDtoGrid.setMaxHeight("250px");

        attractionDetailsDtoGrid.asSingleSelect().addValueChangeListener(event ->
                attractionDto = new AttractionDto(
                        attractionDetailsDtoGrid.asSingleSelect().getValue().getLocation_id(),
                        attractionDetailsDtoGrid.asSingleSelect().getValue().getCity(),
                        attractionDetailsDtoGrid.asSingleSelect().getValue().getName(),
                        attractionDetailsDtoGrid.asSingleSelect().getValue().getDescription(),
                        attractionDetailsDtoGrid.asSingleSelect().getValue().getCategory(),
                        attractionDetailsDtoGrid.asSingleSelect().getValue().getTitle(),
                        (double) attractionDetailsDtoGrid.asSingleSelect().getValue().getPriceEuro()
                ));
    }

    private void setButtons() {

        save.addClickListener(event -> {
            try {
                save();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }

    private void save() throws BackendRequestException {

        attractionService.saveAttractionDto(attractionDto);
        attractionView.refresh();
        setAttractionDto(null);
    }

    private void cancel() {

        attractionView.refresh();
        setAttractionDto(null);
    }

    public void setAttractionDto(AttractionDto attractionDto) {

        if (attractionDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            searchField.focus();
        }
    }

    private void lengthCheck(String enteredText) {

        if (enteredText.length() > 3) {
            locationSearchDtoGrid.setItems(touristAttractionApiService.getAllSearchedLocations(enteredText));
        } else {
            locationSearchDtoGrid.setItems(new LocationSearchDto());
        }
    }
}
