package com.crud.adventuretravel.view;

import com.crud.adventuretravel.domain.AttractionDto;
import com.crud.adventuretravel.form.AttractionForm.NewAttractionForm;
import com.crud.adventuretravel.form.AttractionForm.UpdateAttractionForm;
import com.crud.adventuretravel.service.AttractionService;
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

@PageTitle("Attractions")
@Route(value = "attractions", layout = MainLayout.class)
public class AttractionView extends VerticalLayout {


    private AttractionService attractionService = AttractionService.getInstance();
    private Grid<AttractionDto> grid = new Grid<>(AttractionDto.class);
    private TextField filter = new TextField();
    private NewAttractionForm newAttractionForm = new NewAttractionForm(this);
    private UpdateAttractionForm updateAttractionForm = new UpdateAttractionForm(this);
    private Button addNewAttraction = new Button("Add new Attraction");
    private Button updateAttraction = new Button("Update Attraction");


    public AttractionView() {

        setFilter();

        grid.setColumns("id", "name", "city", "description", "priceEuro", "pricePln");

        addNewAttraction.addClickListener(e -> {
            updateAttractionForm.setAttractionDto(null);
            newAttractionForm.setAttractionDto(new AttractionDto());
        });

        updateAttraction.addClickListener(e -> {
            newAttractionForm.setAttractionDto(null);
            updateAttractionForm.setAttractionDto(new AttractionDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewAttraction, updateAttraction);

        HorizontalLayout mainContent = new HorizontalLayout(grid, newAttractionForm, updateAttractionForm);

        mainContent.setSizeFull();
        grid.setSizeFull();

        add(toolbar, mainContent);

        newAttractionForm.setAttractionDto(null);
        updateAttractionForm.setAttractionDto(null);

        setSizeFull();
        refresh();

        grid.asSingleSelect().addValueChangeListener(event -> {
            newAttractionForm.setAttractionDto(null);
            updateAttractionForm.setAttractionDto(grid.asSingleSelect().getValue());
        });
    }

    public void setFilter() {
        filter.setPlaceholder("Filter by name...");
        filter.setClearButtonVisible(true);
        filter.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        grid.setItems(attractionService.findByName(filter.getValue()));
    }
    public void refresh() {
        grid.setItems(attractionService.getAllAttractions());
    }

}

