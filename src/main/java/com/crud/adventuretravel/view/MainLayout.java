package com.crud.adventuretravel.view;


import com.crud.adventuretravel.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class MainLayout extends AppLayout {

    private H2 viewTitle;
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {

        this.securityService = securityService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addDrawerContent() {

        H1 appName = new H1("Adventure Travel CRM");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private void addHeaderContent() {

        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        logout.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("right", "var(--lumo-space-l)").set("margin", "0")
                .set("position", "absolute");

        addToNavbar(true, toggle, viewTitle, logout);
    }

    private SideNav createNavigation() {

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Main", MainView.class, LineAwesomeIcon.GLOBE_EUROPE_SOLID.create()));
        nav.addItem(new SideNavItem("Reservations", ReservationView.class, LineAwesomeIcon.CLIPBOARD_LIST_SOLID.create()));
        nav.addItem(new SideNavItem("Customers", CustomerView.class, LineAwesomeIcon.USER_FRIENDS_SOLID.create()));
        nav.addItem(new SideNavItem("Tours", TourView.class, LineAwesomeIcon.FLY.create()));
        nav.addItem(new SideNavItem("Attractions", AttractionView.class, LineAwesomeIcon.MONUMENT_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {

        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {

        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {

        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
