package web.views.bilete;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import entity.Bilet;
import repository.RepositoryBilete;
import web.MainLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Vânzare Bilete (POS)")
@Route(value = "pos-bilete", layout = MainLayout.class)
public class VanzareBileteView extends Div {

    private final RepositoryBilete repoBilete = new RepositoryBilete();

    private final List<TicketItem> cosCumparaturi = new ArrayList<>();

    private final Grid<TicketItem> gridCos = new Grid<>(TicketItem.class, false);
    private final H2 totalLabel = new H2("Total: 0.00 RON");

    public VanzareBileteView() {
        setSizeFull();
        addClassNames("pos-view");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(65);

        splitLayout.addToPrimary(createProductSection());
        splitLayout.addToSecondary(createCartSection());

        add(splitLayout);
    }

    // --- PARTEA STÂNGĂ: BUTOANE PRODUSE ---
    private Component createProductSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);

        H2 titlu = new H2("Casierie - Selectare Bilete");

        FlexLayout productsContainer = new FlexLayout();
        productsContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        productsContainer.getStyle().set("gap", "20px");
        productsContainer.setSizeFull();

        productsContainer.add(createTicketButton("Adult", 50.0, VaadinIcon.MALE, "var(--lumo-primary-color)"));
        productsContainer.add(createTicketButton("Copil (<14)", 20.0, VaadinIcon.CHILD, "var(--lumo-success-color)"));
        productsContainer.add(createTicketButton("Student", 30.0, VaadinIcon.ACADEMY_CAP, "var(--lumo-contrast-color)"));
        productsContainer.add(createTicketButton("Pensionar", 25.0, VaadinIcon.USER_HEART, "var(--lumo-tertiary-text-color)"));
        productsContainer.add(createTicketButton("Grup (10+)", 400.0, VaadinIcon.GROUP, "#8b5cf6"));
        productsContainer.add(createTicketButton("VIP Tour", 150.0, VaadinIcon.DIAMOND, "#f59e0b"));

        layout.add(titlu, productsContainer);
        return layout;
    }

    private Component createTicketButton(String nume, double pret, VaadinIcon icon, String culoare) {
        Button btn = new Button();
        btn.setWidth("200px");
        btn.setHeight("180px");

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexLayout.Alignment.CENTER);
        content.setSpacing(true);

        Icon i = icon.create();
        i.setSize("50px");
        i.setColor(culoare);

        Span labelNume = new Span(nume);
        labelNume.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);

        Span labelPret = new Span(pret + " RON");
        labelPret.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.TextColor.SECONDARY);

        content.add(i, labelNume, labelPret);
        btn.getElement().appendChild(content.getElement());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.addClassNames(LumoUtility.BoxShadow.MEDIUM, LumoUtility.Background.BASE);

        btn.addClickListener(e -> adaugaInCos(nume, pret));

        return btn;
    }


    private Component createCartSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addClassNames(LumoUtility.Background.CONTRAST_5);
        layout.setPadding(true);

        H3 titluCos = new H3("Coș Cumpărături");

        // Configurare Tabel Coș
        gridCos.addColumn(TicketItem::getNume).setHeader("Produs").setAutoWidth(true);
        gridCos.addColumn(item -> item.getPret() + " RON").setHeader("Preț")
                .setTextAlign(com.vaadin.flow.component.grid.ColumnTextAlign.END);

        // Buton ștergere per rând
        gridCos.addComponentColumn(item -> {
            Button sterge = new Button(VaadinIcon.TRASH.create());
            sterge.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            sterge.addClickListener(e -> stergeDinCos(item));
            return sterge;
        }).setHeader("").setWidth("70px").setFlexGrow(0);

        gridCos.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        gridCos.setHeightFull();

        VerticalLayout footer = new VerticalLayout();
        footer.setSpacing(true);
        footer.addClassNames(LumoUtility.Background.BASE, LumoUtility.Padding.MEDIUM, LumoUtility.BoxShadow.SMALL);

        totalLabel.addClassNames(LumoUtility.TextColor.PRIMARY);

        Button btnIncaseaza = new Button("ÎNCASEAZĂ", VaadinIcon.CASH.create());
        btnIncaseaza.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btnIncaseaza.setWidthFull();
        btnIncaseaza.setHeight("60px");

        btnIncaseaza.addClickListener(e -> proceseazaPlata());

        footer.add(totalLabel, btnIncaseaza);

        layout.add(titluCos, gridCos, footer);
        return layout;
    }


    private void adaugaInCos(String nume, double pret) {
        cosCumparaturi.add(new TicketItem(nume, pret));
        gridCos.setItems(cosCumparaturi);
        calculeazaTotal();
    }

    private void stergeDinCos(TicketItem item) {
        cosCumparaturi.remove(item);
        gridCos.setItems(cosCumparaturi);
        calculeazaTotal();
    }

    private void calculeazaTotal() {
        double total = cosCumparaturi.stream().mapToDouble(TicketItem::getPret).sum();
        totalLabel.setText("Total: " + String.format("%.2f", total) + " RON");
    }

    private void proceseazaPlata() {
        if (cosCumparaturi.isEmpty()) {
            Notification.show("Coșul este gol!", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            for (TicketItem item : cosCumparaturi) {
                Bilet biletNou = new Bilet();
                biletNou.setTip(item.getNume());
                biletNou.setPret(item.getPret());
                biletNou.setData(LocalDate.now());
                biletNou.setVizitator(null);

                repoBilete.add(biletNou);
            }

            Notification notification = Notification.show("Plată înregistrată! Bon fiscal generat.");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.TOP_CENTER);

            cosCumparaturi.clear();
            gridCos.setItems(cosCumparaturi);
            calculeazaTotal();

        } catch (Exception e) {
            Notification.show("Eroare la salvare: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }

    // Clasă ajutătoare pentru coș (DTO)
    public static class TicketItem {
        private String nume;
        private double pret;

        public TicketItem(String nume, double pret) {
            this.nume = nume;
            this.pret = pret;
        }
        public String getNume() { return nume; }
        public double getPret() { return pret; }
    }
}