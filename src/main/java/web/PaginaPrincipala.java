package web;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import repository.RepositoryAnimale;
import repository.RepositoryIngrijitori;
import repository.RepositoryZone;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Acasă | Zoo Manager")
public class PaginaPrincipala extends VerticalLayout {

    public PaginaPrincipala() {
        addClassName("home-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        add(new H1("Bine ai venit la Zoo Manager!"));
        add(new Span("O privire rapidă asupra grădinii zoologice astăzi."));

        add(createStatsRow());
    }

    private Component createStatsRow() {

        FlexLayout layout = new FlexLayout();
        layout.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        layout.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Gap.LARGE);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);


        RepositoryAnimale repoAnimale = new RepositoryAnimale();
        int nrAnimale = repoAnimale.getAll().size();

        layout.add(createCard("Animale", nrAnimale, VaadinIcon.BUG, "var(--lumo-primary-color)"));


        RepositoryZone repoZone = new RepositoryZone();
        int nrZone = repoZone.getAll().size();
        layout.add(createCard("Zone", nrZone, VaadinIcon.MAP_MARKER, "var(--lumo-success-color)"));


        RepositoryIngrijitori repoIngrijitori = new RepositoryIngrijitori();
        int nrIngrijitori = repoIngrijitori.getAll().size();
        layout.add(createCard("Îngrijitori", nrIngrijitori, VaadinIcon.USER, "var(--lumo-error-color)"));

        return layout;
    }

    private Component createCard(String titlu, int valoare, VaadinIcon icon, String culoare) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.LARGE,
                LumoUtility.AlignItems.CENTER
        );

        card.setWidth("250px");

        Icon i = icon.create();
        i.setSize("40px");
        i.setColor(culoare);

        H2 numar = new H2(String.valueOf(valoare));
        numar.addClassNames(LumoUtility.Margin.Vertical.NONE);

        Span eticheta = new Span(titlu);
        eticheta.addClassNames(LumoUtility.TextColor.SECONDARY);

        card.add(i, numar, eticheta);
        return card;
    }
}