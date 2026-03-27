package web;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import web.views.animale.NavigareGridAnimaleView;
import web.views.bilete.NavigareGridBileteView;
import web.views.bilete.VanzareBileteView;
import web.views.ingrijitori.NavigareGridIngrijitoriView;
import web.views.rapoarte.RapoarteView;
import web.views.vizitatori.NavigareGridVizitatoriView;
import web.views.zone.NavigareGridZoneView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        H1 title = new H1("Zoo Manager App");
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, title);
    }

    private void addDrawerContent() {
        Span appName = new Span("Meniu Principal");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);
        header.addClassNames(LumoUtility.Padding.MEDIUM);


        SideNav nav = new SideNav();


        nav.addItem(new SideNavItem("Acasă", PaginaPrincipala.class, VaadinIcon.HOME.create()));

        nav.addItem(new SideNavItem("Animale", NavigareGridAnimaleView.class, VaadinIcon.BUG.create()));

        nav.addItem(new SideNavItem("Zone", NavigareGridZoneView.class, VaadinIcon.MAP_MARKER.create()));
        nav.addItem(new SideNavItem("Îngrijitori", NavigareGridIngrijitoriView.class, VaadinIcon.USER.create()));
        //nav.addItem(new SideNavItem("Vizitatori", NavigareGridVizitatoriView.class, VaadinIcon.GROUP.create()));
        nav.addItem(new SideNavItem("Vânzare Bilete (POS)", VanzareBileteView.class, VaadinIcon.CASH.create()));
        nav.addItem(new SideNavItem("Istoric Bilete", NavigareGridBileteView.class, VaadinIcon.CLIPBOARD_TEXT.create()));
        nav.addItem(new SideNavItem("Rapoarte", RapoarteView.class, VaadinIcon.CHART.create()));

        Scroller scroller = new Scroller(nav);
        addToDrawer(header, scroller);
    }
}