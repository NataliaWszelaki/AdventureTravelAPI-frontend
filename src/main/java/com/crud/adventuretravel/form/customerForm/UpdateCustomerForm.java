package com.crud.adventuretravel.form.customerForm;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.CustomerDto;
import com.crud.adventuretravel.form.Notifications;
import com.crud.adventuretravel.service.CustomerService;
import com.crud.adventuretravel.service.ReferentialIntegrityViolationException;
import com.crud.adventuretravel.view.CustomerView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;

public class UpdateCustomerForm extends FormLayout {

    private CustomerView simpleView;
    private CustomerService customerService = CustomerService.getInstance();
    private Notifications notifications = new Notifications();
    private boolean isAdmin;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private TextField phoneNumber = new TextField("Phone Number");
    private Checkbox checkboxSubscriber = new Checkbox("Subscription");

    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Button deactivate = new Button("Deactivate");
    private Button cancel = new Button("Cancel");
    private Binder<CustomerDto> binder = new Binder<>(CustomerDto.class);


    public UpdateCustomerForm(CustomerView simpleView) {

        setAuthenticationContext();
        createFormLayout();
        setButtons();
        this.simpleView = simpleView;
    }

    private void setAuthenticationContext() {

        AuthenticationContext authContext = new AuthenticationContext();
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        });
    }

    private void createFormLayout() {

        firstName.setRequiredIndicatorVisible(true);
        lastName.setRequiredIndicatorVisible(true);
        phoneNumber.setRequiredIndicatorVisible(true);

        add(firstName, lastName, phoneNumber, checkboxSubscriber, new HorizontalLayout(update, delete, deactivate, cancel));
        binder.bindInstanceFields(this);
    }

    private void setButtons() {

        update.addClickListener(event -> {
            try {
                update();
            } catch (BackendRequestException e) {
                notifications.showNotification(e.getMessage());
            } catch (NullPointerException e) {
                notifications.showNotification("Please fill in all fields marked with a dot.");
            }
        });
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        update.addClickShortcut(Key.ENTER);

        if (!isAdmin) delete.setVisible(false);
        delete.addClickListener(event -> {
            try {
                delete();
            } catch (BackendRequestException | ReferentialIntegrityViolationException e) {
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

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);
    }

    private void update() throws BackendRequestException {

        CustomerDto customerDto = binder.getBean();
        if (isNotNull(customerDto)) {
            boolean isSubscriber = checkboxSubscriber.getValue();
            customerDto.setSubscriber(isSubscriber);
            customerService.updateCustomerDto(customerDto);
            simpleView.refresh();
            setCustomerDto(null);
        } else {
            notifications.showNotification("Please fill in all fields marked with a dot.");
        }
    }

    private void delete() throws BackendRequestException, ReferentialIntegrityViolationException {

        CustomerDto customerDto = binder.getBean();
        customerService.deleteCustomerDto(customerDto);
        simpleView.refresh();
        setCustomerDto(null);
    }

    private void deactivate() throws BackendRequestException {

        CustomerDto customerDto = binder.getBean();
        customerDto.setSubscriber(false);
        customerService.updateCustomerDtoDeactivate(customerDto);
        simpleView.refresh();
        setCustomerDto(null);
    }

    private void cancel() {

        simpleView.refresh();
        setCustomerDto(null);
    }

    public void setCustomerDto(CustomerDto customerDto) {

        binder.setBean(customerDto);
        if (customerDto == null) {
            setVisible(false);
        } else {
            checkboxSubscriber.setValue(customerDto.isSubscriber());
            setVisible(true);
            phoneNumber.focus();
        }
    }

    private boolean isNotNull(CustomerDto customerDto) {

        String firstName = customerDto.getFirstName();
        String lastName = customerDto.getLastName();
        int phoneNumber = customerDto.getPhoneNumber();
        return !firstName.equals("") && !lastName.equals("") && phoneNumber != 0;
    }
}
