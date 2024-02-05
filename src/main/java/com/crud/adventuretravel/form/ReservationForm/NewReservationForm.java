package com.crud.adventuretravel.form.ReservationForm;

import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.*;
import com.crud.adventuretravel.form.ErrorNotification;
import com.crud.adventuretravel.service.AttractionService;
import com.crud.adventuretravel.service.CustomerService;
import com.crud.adventuretravel.service.ReservationService;
import com.crud.adventuretravel.service.TourService;
import com.crud.adventuretravel.view.ReservationView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class NewReservationForm extends FormLayout {

    private ReservationView reservationView;

    private ComboBox<TourDto> tourComboBox = new ComboBox<>("Tour");
    private ComboBox<CustomerDto> customerComboBox = new ComboBox<>("Customer");
    private MultiSelectComboBox<AttractionDto> attractionDtoComboBox = new MultiSelectComboBox<>("Additional Attractions");
    private DatePicker reservationDate = new DatePicker("Reservation Date");
    private ComboBox<PaymentStatus> paymentStatusComboBox = new ComboBox<>("Payment Status");
    private ComboBox<ReservationStatus> reservationStatusComboBox = new ComboBox<>("Reservation Status");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");

    private ReservationService reservationService = ReservationService.getInstance();
    private ErrorNotification errorNotification = new ErrorNotification();

    public NewReservationForm(ReservationView reservationView) {

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        setData();
        setButtons();
        add(tourComboBox, customerComboBox, attractionDtoComboBox, reservationDate, paymentStatusComboBox,
                reservationStatusComboBox, buttons);
        this.reservationView = reservationView;
    }

    private void setButtons() {

        save.addClickListener(event -> {
            long tourId = tourComboBox.getValue().getId();
            long customerId = customerComboBox.getValue().getId();
            Set<AttractionDto> selectedAttractionsSet = attractionDtoComboBox.getSelectedItems();
            LocalDate selectedDate = reservationDate.getValue();
            PaymentStatus selectedPaymentStatus = paymentStatusComboBox.getValue();
            ReservationStatus selectedReservationStatus = reservationStatusComboBox.getValue();

            try {
                save(tourId, customerId, selectedAttractionsSet, selectedDate, selectedPaymentStatus, selectedReservationStatus);
            } catch (BackendRequestException e) {
                errorNotification.showNotification("Reservation already exists!");
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        cancel.addClickListener(event -> cancel());
        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }

    private void save(long tourId, long customerId, Set<AttractionDto> selectedAttractionsSet,
                      LocalDate selectedDate, PaymentStatus selectedPaymentStatus, ReservationStatus selectedReservationStatus)
            throws BackendRequestException {

        Set<Long> attractionLongSet = selectedAttractionsSet.stream()
                .map(AttractionDto::getId)
                .collect(Collectors.toSet());
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setTourId(tourId);
        reservationDto.setCustomerId(customerId);
        reservationDto.setAttractionLongSet(attractionLongSet);
        reservationDto.setReservationDate(selectedDate);
        reservationDto.setPaymentStatus(selectedPaymentStatus);
        reservationDto.setReservationStatus(selectedReservationStatus);

        reservationService.saveReservationDto(reservationDto);
        reservationView.refresh();
        setReservationDto(null);
    }

    private void cancel() {
        reservationView.refresh();
        setReservationDto(null);
    }

    public void setReservationDto(ReservationDto reservationDto) {

        if (reservationDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            reservationDate.focus();
        }
    }

    public void setData() {

        tourComboBox.setItems(TourService.getInstance().getAllTours());
        tourComboBox.setItemLabelGenerator(TourDto::getName);
        tourComboBox.setAllowCustomValue(true);

        customerComboBox.setItems(CustomerService.getInstance().getAllCustomers());
        customerComboBox.setItemLabelGenerator(CustomerDto::getFullName);
        customerComboBox.setAllowCustomValue(true);

        attractionDtoComboBox.setItems(AttractionService.getInstance().getAllAttractions());
        attractionDtoComboBox.setItemLabelGenerator(AttractionDto::getName);
        attractionDtoComboBox.setAllowCustomValue(true);

        paymentStatusComboBox.setItems(PaymentStatus.values());
        reservationStatusComboBox.setItems(ReservationStatus.values());
    }
}
