package com.crud.adventuretravel.form.CustomerForm;

import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.CustomerDto;
import com.crud.adventuretravel.form.ErrorNotification;
import com.crud.adventuretravel.service.CustomerService;
import com.crud.adventuretravel.view.CustomerView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class NewCustomerForm extends FormLayout {

    private CustomerView simpleView;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private TextField phoneNumber = new TextField("Phone Number");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Binder<CustomerDto> binder = new Binder<CustomerDto>(CustomerDto.class);
    private CustomerService service = CustomerService.getInstance();
    private ErrorNotification errorNotification = new ErrorNotification();

    public NewCustomerForm(CustomerView simpleView) {

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        setButtons();
        add(firstName, lastName, email, phoneNumber, buttons);
        binder.bindInstanceFields(this);
        this.simpleView = simpleView;
    }

    private void setButtons() {

        save.addClickListener(event -> {
            try {
                save();
            } catch (BackendRequestException e) {
                errorNotification.showNotification("Customer already exists!");
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
        service.saveCustomerDto(customerDto);
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
            setVisible(true);
            email.focus();
        }
    }
}
