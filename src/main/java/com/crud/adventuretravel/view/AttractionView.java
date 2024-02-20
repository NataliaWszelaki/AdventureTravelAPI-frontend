package com.crud.adventuretravel.view;

import com.crud.adventuretravel.domain.AttractionDto;
import com.crud.adventuretravel.domain.RateExchangeDto;
import com.crud.adventuretravel.form.attractionForm.NewAttractionForm;
import com.crud.adventuretravel.form.attractionForm.UpdateAttractionForm;
import com.crud.adventuretravel.service.AttractionService;
import com.crud.adventuretravel.service.RateExchangeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Attractions")
@Route(value = "attractions", layout = MainLayout.class)
public class AttractionView extends VerticalLayout {

    private AttractionService attractionService = AttractionService.getInstance();
    private Grid<AttractionDto> grid = new Grid<>();
    private TextField filter = new TextField();
    private NewAttractionForm newAttractionForm = new NewAttractionForm(this);
    private UpdateAttractionForm updateAttractionForm = new UpdateAttractionForm(this);
    private Button addNewAttraction = new Button("Add new Attraction");

    private SplitLayout splitLayout;

    public AttractionView() {

        setSizeFull();
        setFilter();
        addColumnsToGrid();
        setForms();

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewAttraction);
        HorizontalLayout  forms = new HorizontalLayout(newAttractionForm, updateAttractionForm);
        splitLayout = new SplitLayout(grid, forms);
        splitLayout.setSplitterPosition(100);
        splitLayout.setSizeFull();

        add(toolbar, splitLayout);

        refresh();
    }

    private void setFilter() {

        filter.setPlaceholder("Filter by city");
        filter.setClearButtonVisible(true);
        filter.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> grid.setItems(attractionService.findByCity(filter.getValue())));
    }

    private void addColumnsToGrid() {

        grid.addColumn(AttractionDto::getId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(AttractionDto::getCity).setHeader("City").setAutoWidth(true);
        grid.addColumn(AttractionDto::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(AttractionDto::getDescription).setHeader("Description").setWidth("240px");
        grid.addColumn(AttractionDto::getCategory).setHeader("Category").setAutoWidth(true);
        grid.addColumn(AttractionDto::getTitle).setHeader("Title").setAutoWidth(true);
        grid.addColumn(AttractionDto::getPriceEuro).setHeader("Price EURO").setAutoWidth(true);
        grid.addColumn(AttractionView::getRateExchange).setHeader("Price PLN").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();
    }

    private void setForms() {

        newAttractionForm.setAttractionDto(null);
        updateAttractionForm.setAttractionDto(null);

        addNewAttraction.addClickListener(e -> {
            updateAttractionForm.setAttractionDto(null);
            splitLayout.setSplitterPosition(30);
            newAttractionForm.setAttractionDto(new AttractionDto());
        });

        grid.asSingleSelect().addValueChangeListener(event -> {
            newAttractionForm.setAttractionDto(null);
            splitLayout.setSplitterPosition(60);
            updateAttractionForm.setAttractionDto(grid.asSingleSelect().getValue());
        });
    }

    public void refresh() {

        grid.setItems(attractionService.getAllAttractions());
        splitLayout.setSplitterPosition(100);
    }

    private static String getRateExchange(AttractionDto attractionDto) {

        RateExchangeDto rateExchangeDto = RateExchangeService.getInstance().getRateExchange();
        double pricePLN = Math.ceil(attractionDto.getPriceEuro() * rateExchangeDto.getPln());
        return Double.toString(pricePLN);
    }
}

