package com.crud.adventuretravel.form.reservationForm;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.*;
import com.crud.adventuretravel.form.Notifications;
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
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class NewReservationForm extends FormLayout {

    private ReservationView reservationView;
    private ReservationService reservationService = ReservationService.getInstance();
    private Notifications notifications = new Notifications();

    private ComboBox<TourDto> tourComboBox = new ComboBox<>("Tour");
    private ComboBox<CustomerDto> customerComboBox = new ComboBox<>("Customer");
    private MultiSelectComboBox<AttractionDto> attractionDtoComboBox = new MultiSelectComboBox<>("Additional Attractions");
    private ComboBox<PaymentStatus> paymentStatusComboBox = new ComboBox<>("Payment Status");
    private ComboBox<ReservationStatus> reservationStatusComboBox = new ComboBox<>("Reservation Status");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");


    public NewReservationForm(ReservationView reservationView) {

        createFormLayout();
        setButtons();
        this.reservationView = reservationView;
    }

    private void createFormLayout() {

        tourComboBox.setItems(TourService.getInstance().getAllTours());
        tourComboBox.setItemLabelGenerator(TourDto::getName);
        tourComboBox.setAllowCustomValue(true);
        tourComboBox.setRequiredIndicatorVisible(true);

        customerComboBox.setItems(CustomerService.getInstance().getAllCustomers());
        customerComboBox.setItemLabelGenerator(CustomerDto::getFullName);
        customerComboBox.setAllowCustomValue(true);
        customerComboBox.setRequiredIndicatorVisible(true);

        attractionDtoComboBox.setItems(AttractionService.getInstance().getAllAttractions());
        attractionDtoComboBox.setItemLabelGenerator(AttractionDto::getCityAndName);
        attractionDtoComboBox.setAllowCustomValue(true);

        paymentStatusComboBox.setItems(PaymentStatus.values());
        paymentStatusComboBox.setRequiredIndicatorVisible(true);

        reservationStatusComboBox.setItems(ReservationStatus.values());
        paymentStatusComboBox.setRequiredIndicatorVisible(true);

        add(tourComboBox, customerComboBox, attractionDtoComboBox, paymentStatusComboBox,
                reservationStatusComboBox, new HorizontalLayout(save, cancel));
    }

    private void setButtons() {

        save.addClickListener(event -> {
            try {
                save();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            } catch (NullPointerException e) {
                notifications.showNotification("Please fill in all fields marked with a dot.");
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);
    }

    private void save() throws BackendRequestException {

        reservationService.saveReservationDto(fetchDataFromForm());
        clearForm();
        reservationView.refresh();
        setReservationDto(null);

    }

    private void cancel() {

        clearForm();
        reservationView.refresh();
        setReservationDto(null);
    }

    public void setReservationDto(ReservationDto reservationDto) {

        if (reservationDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            tourComboBox.focus();
        }
    }

    private ReservationDto fetchDataFromForm() {

        Set<Long> attractionLongSet = attractionDtoComboBox.getSelectedItems().stream()
                .map(AttractionDto::getId)
                .collect(Collectors.toSet());

        return new ReservationDto(
                tourComboBox.getValue().getId(),
                customerComboBox.getValue().getId(),
                attractionLongSet,
                LocalDate.now(),
                paymentStatusComboBox.getValue(),
                reservationStatusComboBox.getValue()
        );
    }

    private void clearForm() {

        tourComboBox.clear();
        customerComboBox.clear();
        attractionDtoComboBox.clear();
        paymentStatusComboBox.clear();
        reservationStatusComboBox.clear();
    }
}
