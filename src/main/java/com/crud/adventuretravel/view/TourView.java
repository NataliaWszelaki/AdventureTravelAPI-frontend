package com.crud.adventuretravel.view;

import com.crud.adventuretravel.domain.RateExchangeDto;
import com.crud.adventuretravel.domain.TourDto;
import com.crud.adventuretravel.form.tourForm.NewTourForm;
import com.crud.adventuretravel.form.tourForm.UpdateTourForm;
import com.crud.adventuretravel.service.RateExchangeService;
import com.crud.adventuretravel.service.TourService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Tours")
@Route(value = "tours", layout = MainLayout.class)
public class TourView extends VerticalLayout {

    private TourService tourService = TourService.getInstance();
    private Grid<TourDto> grid = new Grid<>();
    private TextField filter = new TextField();
    private NewTourForm newTourForm = new NewTourForm(this);
    private UpdateTourForm updateTourForm = new UpdateTourForm(this);
    private Button addNewTour = new Button("Add new Tour");

    public TourView() {

        setSizeFull();
        setFilter();
        addColumnsToGrid();
        setForms();

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewTour);
        HorizontalLayout mainContent = new HorizontalLayout(grid, newTourForm, updateTourForm);
        mainContent.setSizeFull();
        add(toolbar, mainContent);

        refresh();
    }

    private void setFilter() {

        filter.setPlaceholder("Filter by name");
        filter.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> grid.setItems(tourService.findByName(filter.getValue())));
    }

    private void addColumnsToGrid() {

        grid.addColumn(TourDto::getId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(TourDto::getName).setHeader("Name").setWidth("150px");
        grid.addColumn(TourDto::getCountry).setHeader("Country").setAutoWidth(true);
        grid.addColumn(TourDto::getDescription).setHeader("Description").setWidth("220px");
        grid.addColumn(TourDto::getStartDate).setHeader("Start Date").setAutoWidth(true);
        grid.addColumn(TourDto::getEndDate).setHeader("End Date").setAutoWidth(true);
        grid.addColumn(TourDto::getStartLocation).setHeader("Start Location").setAutoWidth(true);
        grid.addColumn(TourDto::getEndLocation).setHeader("End Location").setAutoWidth(true);
        grid.addColumn(TourDto::getPriceEuro).setHeader("Price Euro").setAutoWidth(true);
        grid.addColumn(TourView::getRateExchange).setHeader("Price PLN").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();
    }

    private void setForms() {

        newTourForm.setTourDto(null);
        updateTourForm.setTourDto(null);

        addNewTour.addClickListener(e -> {
            updateTourForm.setTourDto(null);
            newTourForm.setTourDto(new TourDto());
        });

        grid.asSingleSelect().addValueChangeListener(event -> {
            newTourForm.setTourDto(null);
            updateTourForm.setTourDto(grid.asSingleSelect().getValue());
        });
    }

    public void refresh() {

        grid.setItems(tourService.getAllTours());
    }

    private static String getRateExchange(TourDto tourDto) {

        RateExchangeDto rateExchangeDto = RateExchangeService.getInstance().getRateExchange();
        double pricePLN = Math.ceil(tourDto.getPriceEuro() * rateExchangeDto.getPln());
        return Double.toString(pricePLN);
    }
}
