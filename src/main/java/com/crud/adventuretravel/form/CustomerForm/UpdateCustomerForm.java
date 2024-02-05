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

public class UpdateCustomerForm extends FormLayout {

    private CustomerView simpleView;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private TextField phoneNumber = new TextField("Phone Number");
    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");
    private Binder<CustomerDto> binder = new Binder<CustomerDto>(CustomerDto.class);
    private CustomerService service = CustomerService.getInstance();
    private ErrorNotification errorNotification = new ErrorNotification();

    public UpdateCustomerForm(CustomerView simpleView) {

        HorizontalLayout buttons = new HorizontalLayout(update, delete, cancel);
        setButtons();
        add(firstName, lastName, phoneNumber, buttons);
        binder.bindInstanceFields(this);
        this.simpleView = simpleView;
    }

    private void setButtons() {

        update.addClickListener(event -> {
            try {
                update();
            } catch (BackendRequestException e) {
                errorNotification.showNotification("Customer doesn't exist.");
            }
        });
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        update.addClickShortcut(Key.ENTER);

        delete.addClickListener(event -> delete());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        cancel.addClickListener(event -> cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickShortcut(Key.ESCAPE);
    }

    private void update() throws BackendRequestException {
        CustomerDto customerDto = binder.getBean();
        service.updateCustomerDto(customerDto);
        simpleView.refresh();
        setCustomerDto(null);
    }

    private void delete() {
        CustomerDto customerDto = binder.getBean();
        service.deleteCustomerDto(customerDto);
        simpleView.refresh();
        setCustomerDto(null);
    }


    private void cancel() {
        simpleView.refresh();
        setCustomerDto(null);
    }

    public void setCustomerDto(CustomerDto customerDto) {
        binder.setBean(customerDto);

        setVisible(customerDto != null);
    }
}
