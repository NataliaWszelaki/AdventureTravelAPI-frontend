package com.crud.adventuretravel.view;

import com.crud.adventuretravel.domain.CustomerDto;
import com.crud.adventuretravel.form.CustomerForm.NewCustomerForm;
import com.crud.adventuretravel.form.CustomerForm.UpdateCustomerForm;
import com.crud.adventuretravel.service.CustomerService;
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

@PageTitle("Customers")
@Route(value = "customers", layout = MainLayout.class)
public class CustomerView extends VerticalLayout {

    private CustomerService customerService = CustomerService.getInstance();
    private Grid<CustomerDto> grid = new Grid<>(CustomerDto.class);
    private TextField filter = new TextField();
    private NewCustomerForm newCustomerForm = new NewCustomerForm(this);
    private UpdateCustomerForm updateCustomerForm = new UpdateCustomerForm(this);
    private Button addNewCustomer = new Button("Add new Customer");
    private Button updateCustomer = new Button("Update Customer");


    public CustomerView() {

        setFilter();

        grid.setColumns("id", "firstName", "lastName", "email", "phoneNumber", "accountCreationDate");

        addNewCustomer.addClickListener(e -> {
            updateCustomerForm.setCustomerDto(null);
            newCustomerForm.setCustomerDto(new CustomerDto());
        });

        updateCustomer.addClickListener(e -> {
            newCustomerForm.setCustomerDto(null);
            updateCustomerForm.setCustomerDto(new CustomerDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewCustomer, updateCustomer);

        HorizontalLayout mainContent = new HorizontalLayout(grid, newCustomerForm, updateCustomerForm);
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(toolbar, mainContent);

        newCustomerForm.setCustomerDto(null);
        updateCustomerForm.setCustomerDto(null);


        setSizeFull();
        refresh();

        grid.asSingleSelect().addValueChangeListener(event -> {
            newCustomerForm.setCustomerDto(null);
            updateCustomerForm.setCustomerDto(grid.asSingleSelect().getValue());
        });
    }

    public void setFilter() {
        filter.setPlaceholder("Filter by email...");
        filter.setClearButtonVisible(true);
        filter.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        grid.setItems(customerService.findByEmail(filter.getValue()));
    }

    public void refresh() {
        grid.setItems(customerService.getAllCustomers());
    }
}
