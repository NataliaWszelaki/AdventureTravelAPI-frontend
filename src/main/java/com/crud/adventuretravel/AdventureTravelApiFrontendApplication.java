package com.crud.adventuretravel;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("my-theme")
public class AdventureTravelApiFrontendApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(AdventureTravelApiFrontendApplication.class, args);
    }
}


