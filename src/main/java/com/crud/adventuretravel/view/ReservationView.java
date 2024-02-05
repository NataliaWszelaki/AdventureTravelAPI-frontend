package com.crud.adventuretravel.view;

import com.crud.adventuretravel.domain.AttractionDto;
import com.crud.adventuretravel.domain.CustomerDto;
import com.crud.adventuretravel.domain.ReservationDto;
import com.crud.adventuretravel.domain.TourDto;
import com.crud.adventuretravel.form.ReservationForm.NewReservationForm;
import com.crud.adventuretravel.form.ReservationForm.UpdateReservationForm;
import com.crud.adventuretravel.service.AttractionService;
import com.crud.adventuretravel.service.CustomerService;
import com.crud.adventuretravel.service.ReservationService;
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

import java.util.Set;
import java.util.stream.Collectors;


@PageTitle("Reservations")
@Route(value = "reservations", layout = MainLayout.class)
public class ReservationView extends VerticalLayout {


    private ReservationService reservationService = ReservationService.getInstance();
    private Grid<ReservationDto> grid = new Grid<>();
    private TextField filter = new TextField();
    private NewReservationForm newReservationForm = new NewReservationForm(this);
    private UpdateReservationForm updateReservationForm = new UpdateReservationForm(this);
    private Button addNewReservation = new Button("Add new Reservation");
    private Button updateReservation = new Button("Update Reservation");


    public ReservationView() {

        setFilter();
        addColumnsToGrid();

        addNewReservation.addClickListener(e -> {
            updateReservationForm.setReservationDto(null);
            newReservationForm.setReservationDto(new ReservationDto());
        });

        updateReservation.addClickListener(e -> {
            newReservationForm.setReservationDto(null);
            updateReservationForm.setReservationDto(new ReservationDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewReservation, updateReservation);

        HorizontalLayout mainContent = new HorizontalLayout(grid, newReservationForm, updateReservationForm);
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(toolbar, mainContent);

        newReservationForm.setReservationDto(null);
        updateReservationForm.setReservationDto(null);

        setSizeFull();
        refresh();

        grid.asSingleSelect().addValueChangeListener(event -> {
            newReservationForm.setReservationDto(null);
            updateReservationForm.setReservationDto(grid.asSingleSelect().getValue());
        });
    }

    public void setFilter() {

        filter.setPlaceholder("Filter by Customer's name...");
        filter.setClearButtonVisible(true);
        filter.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        grid.setItems(reservationService.findByName(filter.getValue()));
    }

    public void addColumnsToGrid() {

        grid.addColumn(ReservationDto::getId).setHeader("Id");
        grid.addColumn(ReservationView::formatTourName).setHeader("Tour");
        grid.addColumn(ReservationView::formatCustomerName).setHeader("Customer");
        grid.addColumn(ReservationView::formatAttractionName).setHeader("Additional Attractions");
        grid.addColumn(ReservationDto::getReservationDate).setHeader("Reservation Date");
        grid.addColumn(ReservationDto::getPaymentStatus).setHeader("Payment Status");
        grid.addColumn(ReservationDto::getReservationStatus).setHeader("Reservation Status");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    public void refresh() {

        grid.setItems(reservationService.getAllReservations());
    }

    private static String formatTourName(ReservationDto reservationDto) {

        long tourId = reservationDto.getTourId();
        TourDto tourDto = TourService.getInstance().getTourById(tourId);
        return String.format(tourDto.getName());
    }

    private static String formatCustomerName(ReservationDto reservationDto) {

        long customerId = reservationDto.getCustomerId();
        CustomerDto customerDto = CustomerService.getInstance().getCustomerById(customerId);
        return String.format(customerDto.getFullName());
    }

    private static String formatAttractionName(ReservationDto reservationDto) {

        Set<Long> attractionLongSet = reservationDto.getAttractionLongSet();
        Set<AttractionDto> attractionDtoSet = attractionLongSet.stream()
                .map(a -> AttractionService.getInstance().getAttractionById(a))
                .collect(Collectors.toSet());
        StringBuilder stringBuilder = new StringBuilder();
        for (AttractionDto attractionDto : attractionDtoSet) {
            stringBuilder.append(attractionDto.getName()).append(". \n");
        }
        return stringBuilder.toString();
    }
}

