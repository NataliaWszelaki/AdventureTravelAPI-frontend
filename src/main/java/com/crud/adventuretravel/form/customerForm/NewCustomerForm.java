package com.crud.adventuretravel.form.customerForm;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.CustomerDto;
import com.crud.adventuretravel.form.Notifications;
import com.crud.adventuretravel.service.CustomerService;
import com.crud.adventuretravel.view.CustomerView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class NewCustomerForm extends FormLayout {

    private CustomerView simpleView;
    private CustomerService service = CustomerService.getInstance();
    private Notifications notifications = new Notifications();

    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private EmailField email = new EmailField("Email");
    private TextField phoneNumber = new TextField("Phone Number");
    private Checkbox checkboxSubscriber = new Checkbox("Subscription");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Binder<CustomerDto> binder = new Binder<CustomerDto>(CustomerDto.class);


    public NewCustomerForm(CustomerView simpleView) {

        createFormLayout();
        setButtons();
        this.simpleView = simpleView;
    }

    private void createFormLayout() {

        firstName.setRequiredIndicatorVisible(true);
        lastName.setRequiredIndicatorVisible(true);
        phoneNumber.setRequiredIndicatorVisible(true);

        add(firstName, lastName, email, phoneNumber, checkboxSubscriber, new HorizontalLayout(save, cancel));
        binder.bindInstanceFields(this);
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
        cancel.addClickShortcut(Key.ESCAPE);
    }

    private void save() throws BackendRequestException {

        CustomerDto customerDto = binder.getBean();
        if (isNotNull(customerDto)) {
            boolean isSubscriber = checkboxSubscriber.getValue();
            customerDto.setSubscriber(isSubscriber);
            service.saveCustomerDto(customerDto);
            simpleView.refresh();
            setCustomerDto(null);
        } else {
            notifications.showNotification("Please fill in all fields marked with a dot.");
        }
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
            setVisible(true);
            email.focus();
        }
    }

    private boolean isNotNull(CustomerDto customerDto) {

        String firstName = customerDto.getFirstName();
        String lastName = customerDto.getLastName();
        String email = customerDto.getEmail();
        int phoneNumber = customerDto.getPhoneNumber();
        return firstName != null && lastName != null && email != null && phoneNumber != 0;
    }
}
