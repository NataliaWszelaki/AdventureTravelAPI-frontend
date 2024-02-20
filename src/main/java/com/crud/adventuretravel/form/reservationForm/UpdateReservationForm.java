package com.crud.adventuretravel.form.reservationForm;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.AttractionDto;
import com.crud.adventuretravel.domain.PaymentStatus;
import com.crud.adventuretravel.domain.ReservationDto;
import com.crud.adventuretravel.domain.ReservationStatus;
import com.crud.adventuretravel.form.Notifications;
import com.crud.adventuretravel.service.AttractionService;
import com.crud.adventuretravel.service.ReservationService;
import com.crud.adventuretravel.view.ReservationView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

public class UpdateReservationForm extends FormLayout {

    private ReservationView reservationView;
    private ReservationService reservationService = ReservationService.getInstance();
    private Notifications notifications = new Notifications();
    private boolean isAdmin;

    private TextField idField = new TextField("Id");
    private MultiSelectComboBox<AttractionDto> attractionDtoComboBox = new MultiSelectComboBox<>("Additional Attractions");
    private ComboBox<PaymentStatus> paymentStatusComboBox = new ComboBox<>("Payment Status");
    private ComboBox<ReservationStatus> reservationStatusComboBox = new ComboBox<>("Reservation Status");

    private Button update = new Button("Update");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    private Button deactivate = new Button("Deactivate");


    public UpdateReservationForm(ReservationView reservationView) {

        setAuthenticationContext();
        createFormLayout();
        setButtons();
        this.reservationView = reservationView;
    }

    private void setAuthenticationContext() {

        AuthenticationContext authContext = new AuthenticationContext();
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        });
    }

    private void createFormLayout() {

        attractionDtoComboBox.setItems(AttractionService.getInstance().getAllAttractions());
        attractionDtoComboBox.setItemLabelGenerator(AttractionDto::getCityAndName);
        attractionDtoComboBox.setAllowCustomValue(true);

        paymentStatusComboBox.setItems(PaymentStatus.values());

        reservationStatusComboBox.setItems(ReservationStatus.values());

        add(attractionDtoComboBox, paymentStatusComboBox, reservationStatusComboBox,
                new HorizontalLayout(update, delete, deactivate, cancel));
    }

    private void setButtons() {

        update.addClickListener(event -> {
            try {
                update();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            }
        });
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        update.addClickShortcut(Key.ENTER);

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);

        if (!isAdmin) delete.setVisible(false);
        delete.addClickListener(event -> {
            long reservationId = Long.parseLong(idField.getValue());
            try {
                delete(reservationId);
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            }
        });
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        if (!isAdmin) deactivate.setVisible(false);
        deactivate.addClickListener(event -> {
            try {
                deactivate();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            }
        });
        deactivate.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }

    private void update() throws BackendRequestException {

        reservationService.updateReservationDto(fetchDataFromForm());
        setReservationDto(null);
        clearForm();
        reservationView.refresh();
    }

    private void delete(long reservationId) throws BackendRequestException {

        reservationService.deleteReservationDto(reservationId);
        setReservationDto(null);
        clearForm();
        reservationView.refresh();
    }

    private void deactivate() throws BackendRequestException {

        reservationService.updateReservationDtoDeactivate(fetchDataFromForm());
        reservationView.refresh();
        setReservationDto(null);
        clearForm();
    }

    private void cancel() {

        reservationView.refresh();
        createFormLayout();
        setReservationDto(null);
    }

    public void setReservationDto(ReservationDto reservationDto) {

        if (reservationDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            idField.setValue(String.valueOf(reservationDto.getId()));
            attractionDtoComboBox.setValue(reservationDto.getAttractionLongSet().stream()
                    .map(id -> AttractionService.getInstance().getAttractionById(id))
                    .collect(Collectors.toSet()));
            paymentStatusComboBox.setValue(reservationDto.getPaymentStatus());
            reservationStatusComboBox.setValue(reservationDto.getReservationStatus());
            paymentStatusComboBox.focus();
        }
    }

    private ReservationDto fetchDataFromForm() {

        long reservationId = Long.parseLong(idField.getValue());
        ReservationDto reservationDto = reservationService.getReservationById(reservationId);
        Set<Long> attractionLongSet = attractionDtoComboBox.getSelectedItems().stream()
                .map(AttractionDto::getId)
                .collect(Collectors.toSet());

        return new ReservationDto(
                reservationId,
                reservationDto.getTourId(),
                reservationDto.getCustomerId(),
                attractionLongSet,
                reservationDto.getReservationDate(),
                paymentStatusComboBox.getValue(),
                reservationStatusComboBox.getValue()
        );
    }

    private void clearForm() {

        attractionDtoComboBox.clear();
        paymentStatusComboBox.clear();
        reservationStatusComboBox.clear();
    }
}
