package com.crud.adventuretravel.view;

import com.crud.adventuretravel.domain.CustomerDto;
import com.crud.adventuretravel.form.customerForm.NewCustomerForm;
import com.crud.adventuretravel.form.customerForm.UpdateCustomerForm;
import com.crud.adventuretravel.service.CustomerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Customers")
@Route(value = "customers", layout = MainLayout.class)
public class CustomerView extends VerticalLayout {

    private CustomerService customerService = CustomerService.getInstance();
    private Grid<CustomerDto> grid = new Grid<>();
    private TextField filter = new TextField();
    private NewCustomerForm newCustomerForm = new NewCustomerForm(this);
    private UpdateCustomerForm updateCustomerForm = new UpdateCustomerForm(this);
    private Button addNewCustomer = new Button("Add new Customer");

    public CustomerView() {

        setSizeFull();
        setFilter();
        addColumnsToGrid();
        setForms();

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewCustomer);
        HorizontalLayout mainContent = new HorizontalLayout(grid, newCustomerForm, updateCustomerForm);
        mainContent.setSizeFull();
        add(toolbar, mainContent);

        refresh();
    }

    private void setFilter() {

        filter.setPlaceholder("Filter by email");
        filter.setClearButtonVisible(true);
        filter.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> grid.setItems(customerService.findByEmail(filter.getValue())));
    }

    private void addColumnsToGrid() {

        grid.addColumn(CustomerDto::getId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(CustomerDto::getFirstName).setHeader("First Name").setAutoWidth(true);
        grid.addColumn(CustomerDto::getLastName).setHeader("Last Name").setAutoWidth(true);
        grid.addColumn(CustomerDto::getEmail).setHeader("Email").setWidth("220px").setTooltipGenerator(customerDto -> "Full address email: " + customerDto.getEmail());
        grid.addColumn(CustomerDto::getPhoneNumber).setHeader("Phone Number").setAutoWidth(true);
        grid.addColumn(CustomerDto::getAccountCreationDate).setHeader("Account Creation Date").setAutoWidth(true);
        grid.addColumn(customerDto -> customerDto.isSubscriber() ? "Yes" : "No")
                .setHeader(createSubscriberHeader());
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();
    }

    private void setForms() {

        newCustomerForm.setCustomerDto(null);
        updateCustomerForm.setCustomerDto(null);

        addNewCustomer.addClickListener(e -> {
            updateCustomerForm.setCustomerDto(null);
            newCustomerForm.setCustomerDto(new CustomerDto());
        });

        grid.asSingleSelect().addValueChangeListener(event -> {
            newCustomerForm.setCustomerDto(null);
            updateCustomerForm.setCustomerDto(grid.asSingleSelect().getValue());
        });
    }

    public void refresh() {

        grid.setItems(customerService.getAllCustomers());
    }

    private static Component createSubscriberHeader() {

        Span span = new Span("Subscriber");
        HorizontalLayout layout = new HorizontalLayout(span);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(false);
        return layout;
    }
}
