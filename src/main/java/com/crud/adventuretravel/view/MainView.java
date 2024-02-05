package com.crud.adventuretravel.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Main")
@Route(value = "", layout = MainLayout.class)
public class MainView extends AppLayout {

    public MainView() {
    }
}
