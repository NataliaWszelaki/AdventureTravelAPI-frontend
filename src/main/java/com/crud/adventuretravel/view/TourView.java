package com.crud.adventuretravel.view;

import com.crud.adventuretravel.domain.TourDto;
import com.crud.adventuretravel.form.TourForm.NewTourForm;
import com.crud.adventuretravel.form.TourForm.UpdateTourForm;
import com.crud.adventuretravel.service.TourService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Tours")
@Route(value = "tours", layout = MainLayout.class)
public class TourView extends VerticalLayout {

    private TourService tourService = TourService.getInstance();
    private Grid<TourDto> grid = new Grid<>(TourDto.class);
    private TextField filter = new TextField();
    private NewTourForm newTourForm = new NewTourForm(this);
    private UpdateTourForm updateTourForm = new UpdateTourForm(this);
    private Button addNewTour = new Button("Add new Tour");
    private Button updateTour = new Button("Update Tour");


    public TourView() {

        setFilter();

        grid.setColumns("id", "name", "country", "description", "startDate", "endDate", "startLocation", "endLocation", "priceEuro", "pricePln");

        addNewTour.addClickListener(e -> {
            updateTourForm.setTourDto(null);
            newTourForm.setTourDto(new TourDto());
        });

        updateTour.addClickListener(e -> {
            newTourForm.setTourDto(null);
            updateTourForm.setTourDto(new TourDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewTour, updateTour);

        HorizontalLayout mainContent = new HorizontalLayout(grid, newTourForm, updateTourForm);
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(toolbar, mainContent);

        newTourForm.setTourDto(null);
        updateTourForm.setTourDto(null);


        setSizeFull();
        refresh();

        grid.asSingleSelect().addValueChangeListener(event -> {
            newTourForm.setTourDto(null);
            updateTourForm.setTourDto(grid.asSingleSelect().getValue());
        });
    }

    public void setFilter() {
        filter.setPlaceholder("Filter by name...");
        filter.setClearButtonVisible(true);
        filter.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        grid.setItems(tourService.findByName(filter.getValue()));
    }

    public void refresh() {
        grid.setItems(tourService.getAllTours());
    }
}
