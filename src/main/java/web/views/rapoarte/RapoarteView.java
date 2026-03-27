package web.views.rapoarte;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.DetachEvent;

import entity.Animal;
import entity.Ingrijitor;
import repository.RepositoryAnimale;
import repository.RepositoryIngrijitori;
import service.RaportService;
import web.ApexChartWrapper;
import web.MainLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Rapoarte și Statistici")
@Route(value = "rapoarte", layout = MainLayout.class)
public class RapoarteView extends VerticalLayout {

    private final RaportService raportService = new RaportService();
    private final RepositoryAnimale repoAnimale = new RepositoryAnimale();
    private final RepositoryIngrijitori repoIngrijitori = new RepositoryIngrijitori();

    private final VerticalLayout dynamicChartContainer = new VerticalLayout();
    private final H2 totalProfitLabel = new H2("Calculează costurile mai sus...");

    private double ultimulTotalSalarii = 0.0;
    private double ultimulTotalHrana = 0.0;

    public RapoarteView() {
        setSpacing(false);
        setPadding(true);
        setSizeFull();

        add(new H2("Centru de Rapoarte Zoo"));

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        tabSheet.add("Infografice & Statistici", createDashboardTab());

        tabSheet.add("Contabilitate & Rapoarte Detaliate", createCalculatorsTab());

        add(tabSheet);
    }


    private Component createDashboardTab() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setPadding(false);

        FlexLayout staticCharts = new FlexLayout();
        staticCharts.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        staticCharts.getStyle().set("gap", "20px");
        staticCharts.add(createAnimalePerZonaChart());
        staticCharts.add(createHranaPerZonaChart());


        H2 titluInteractiv = new H2("Analiză Încasări pe Perioadă");
        titluInteractiv.addClassNames(LumoUtility.Margin.Top.LARGE);

        DatePicker startDate = new DatePicker("De la:");
        startDate.setValue(LocalDate.now().minusDays(30));
        DatePicker endDate = new DatePicker("Până la:");
        endDate.setValue(LocalDate.now());
        Button btnGenereaza = new Button("Generează", VaadinIcon.PIE_CHART.create());
        btnGenereaza.addClickListener(e -> updateIncasariChart(startDate.getValue(), endDate.getValue()));

        HorizontalLayout controls = new HorizontalLayout(startDate, endDate, btnGenereaza);
        controls.setAlignItems(FlexComponent.Alignment.BASELINE);

        dynamicChartContainer.setWidth("100%");
        dynamicChartContainer.setPadding(false);
        updateIncasariChart(startDate.getValue(), endDate.getValue());

        VerticalLayout sectionCard = new VerticalLayout(titluInteractiv, controls, dynamicChartContainer);
        sectionCard.addClassNames(LumoUtility.Background.BASE, LumoUtility.BoxShadow.MEDIUM, LumoUtility.BorderRadius.LARGE, LumoUtility.Padding.MEDIUM);
        sectionCard.setMaxWidth("1000px");

        mainLayout.add(staticCharts, sectionCard);

        Div content = new Div(mainLayout);
        content.setSizeFull();
        content.addClassName(LumoUtility.Overflow.AUTO);
        return content;
    }


    private Component createCalculatorsTab() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.setMaxWidth("950px");


        layout.add(createSectionTitle("A. Estimare Costuri (Salarii & Hrană)"));

        H3 t1 = new H3("1. Calcul Salarii Angajați");
        List<Ingrijitor> staff = repoIngrijitori.getAll();
        double sumaTarife = staff.stream().mapToDouble(Ingrijitor::getTarifOrar).sum();

        Span infoStaff = new Span("Total Angajați: " + staff.size() + " | Sumă Tarife Orare: " + sumaTarife + " RON/h");
        infoStaff.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.TextColor.SECONDARY);

        IntegerField oreLucrate = new IntegerField("Ore/Lună");
        oreLucrate.setValue(160);
        Button btnCalcSalarii = new Button("Calculează", VaadinIcon.MONEY.create());
        Span resSalarii = new Span("0.00 RON");
        resSalarii.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.LARGE);

        btnCalcSalarii.addClickListener(e -> {
            if (oreLucrate.getValue() != null) {
                ultimulTotalSalarii = sumaTarife * oreLucrate.getValue();
                resSalarii.setText(String.format("%.2f RON", ultimulTotalSalarii));
                actualizeazaBalanta();
            }
        });

        HorizontalLayout rowSalarii = new HorizontalLayout(oreLucrate, btnCalcSalarii, new Span("Total:"), resSalarii);
        rowSalarii.setAlignItems(FlexComponent.Alignment.BASELINE);
        layout.add(t1, infoStaff, rowSalarii);

        H3 t2 = new H3("2. Calcul Cost Hrană");
        List<Animal> animale = repoAnimale.getAll();
        double consumZilnic = animale.stream().mapToDouble(a -> a.getCantitateHrana() * a.getFrecventaHrana()).sum();

        Span infoHrana = new Span("Consum Total Zoo: " + String.format("%.2f", consumZilnic) + " kg/zi");
        infoHrana.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.TextColor.SECONDARY);

        NumberField pretMediu = new NumberField("Preț Mediu (RON/kg)");
        pretMediu.setValue(15.0);
        IntegerField zile = new IntegerField("Zile");
        zile.setValue(30);
        Button btnCalcHrana = new Button("Calculează", VaadinIcon.CART.create());
        Span resHrana = new Span("0.00 RON");
        resHrana.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.LARGE);

        btnCalcHrana.addClickListener(e -> {
            if (pretMediu.getValue() != null && zile.getValue() != null) {
                ultimulTotalHrana = consumZilnic * pretMediu.getValue() * zile.getValue();
                resHrana.setText(String.format("%.2f RON", ultimulTotalHrana));
                actualizeazaBalanta();
            }
        });

        HorizontalLayout rowHrana = new HorizontalLayout(pretMediu, zile, btnCalcHrana, new Span("Total:"), resHrana);
        rowHrana.setAlignItems(FlexComponent.Alignment.BASELINE);
        layout.add(t2, infoHrana, rowHrana, createDivider());



        layout.add(createSectionTitle("B. Rapoarte Bilete (Vânzări)"));

        H3 t3 = new H3("3. Statistici Bilete Vândute");

        DatePicker dataVizitatori = new DatePicker("Data:");
        Button cmdVizitatori = new Button("Analizează", VaadinIcon.BAR_CHART.create());

        IntegerField totalBileteField = new IntegerField("Total Bilete:");
        totalBileteField.setReadOnly(true);

        Grid<Map.Entry<String, Integer>> gridTipuri = new Grid<>();
        gridTipuri.addColumn(Map.Entry::getKey).setHeader("Tip Bilet");
        gridTipuri.addColumn(Map.Entry::getValue).setHeader("Nr. Vândute");
        gridTipuri.setHeight("150px");

        cmdVizitatori.addClickListener(e -> {
            if (dataVizitatori.getValue() != null) {
                totalBileteField.setValue(raportService.numarVizitatoriPeZi(dataVizitatori.getValue()));
                gridTipuri.setItems(raportService.statisticaTipBileteZi(dataVizitatori.getValue()).entrySet());
            } else {
                Notification.show("Alege o dată!");
            }
        });

        layout.add(t3, new HorizontalLayout(dataVizitatori, cmdVizitatori, totalBileteField), gridTipuri, createDivider());




        layout.add(createSectionTitle("C. Rapoarte Financiare (Încasări)"));

        H3 t4 = new H3("4. Încasări Zi / Lună");


        DatePicker dataIncasari = new DatePicker("Data Zi:");
        Button cmdZi = new Button("Calc. Zi");
        NumberField resZi = new NumberField("RON:"); resZi.setReadOnly(true);

        IntegerField an = new IntegerField("An");
        an.setValue(LocalDate.now().getYear());
        IntegerField luna = new IntegerField("Lună");
        luna.setValue(LocalDate.now().getMonthValue());
        Button cmdLuna = new Button("Calc. Lună");
        NumberField resLuna = new NumberField("RON:"); resLuna.setReadOnly(true);

        cmdZi.addClickListener(e -> {
            if (dataIncasari.getValue() != null) resZi.setValue(raportService.incasariPeZi(dataIncasari.getValue()));
        });
        cmdLuna.addClickListener(e -> {
            if (an.getValue() != null && luna.getValue() != null)
                resLuna.setValue(raportService.incasariPeLuna(an.getValue(), luna.getValue()));
        });

        layout.add(t4,
                new HorizontalLayout(dataIncasari, cmdZi, resZi),
                new HorizontalLayout(an, luna, cmdLuna, resLuna),
                createDivider());



        layout.add(createSectionTitle("D. Balanță Estimativă (Profit/Pierdere)"));

        VerticalLayout cardBalanta = new VerticalLayout();
        cardBalanta.addClassNames(LumoUtility.Background.BASE, LumoUtility.BoxShadow.MEDIUM, LumoUtility.Padding.LARGE, LumoUtility.BorderRadius.LARGE);

        Double incasariLunaCurenta = raportService.incasariPeLuna(LocalDate.now().getYear(), LocalDate.now().getMonthValue());

        Span lblIncasari = new Span("Venituri Reale (Bilete luna curentă): " + String.format("%.2f RON", incasariLunaCurenta));
        lblIncasari.addClassNames(LumoUtility.TextColor.SUCCESS, LumoUtility.FontWeight.BOLD);

        cardBalanta.add(lblIncasari, new Span("Profit Estimativ (Venituri - Cheltuieli calculate mai sus):"), totalProfitLabel);

        layout.add(cardBalanta);

        return layout;
    }

    private void actualizeazaBalanta() {
        Double incasari = raportService.incasariPeLuna(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        double cheltuieli = ultimulTotalSalarii + ultimulTotalHrana;
        double profit = incasari - cheltuieli;

        totalProfitLabel.setText(String.format("%.2f RON", profit));

        if (profit >= 0) totalProfitLabel.getStyle().set("color", "var(--lumo-success-color)");
        else totalProfitLabel.getStyle().set("color", "var(--lumo-error-color)");
    }


    private Component createSectionTitle(String text) {
        H2 title = new H2(text);
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.TextColor.PRIMARY, LumoUtility.Margin.Top.LARGE);
        return title;
    }

    private Component createDivider() {
        Div d = new Div();
        d.addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.BorderColor.CONTRAST_10, LumoUtility.Margin.Vertical.MEDIUM);
        d.setWidthFull();
        return d;
    }

    private void updateIncasariChart(LocalDate start, LocalDate end) {
        dynamicChartContainer.removeAll();
        if (start == null || end == null || start.isAfter(end)) return;
        Map<String, Double> data = raportService.incasariPeTipSiInterval(start, end);
        if (data.isEmpty()) { dynamicChartContainer.add(new Span("Nu există date.")); return; }
        ApexChartWrapper chart = new ApexChartWrapper("pie", "Încasări (RON)", new ArrayList<>(data.keySet()), new ArrayList<>(data.values()), new String[]{"#EF4444", "#3B82F6", "#10B981", "#F59E0B"});
        chart.setHeight("400px");
        dynamicChartContainer.add(chart);
    }

    private Component createAnimalePerZonaChart() {
        List<Animal> a = repoAnimale.getAll();
        Map<String, Integer> s = new HashMap<>();
        a.forEach(x -> { if(x.getZona()!=null) s.put(x.getZona().getNume(), s.getOrDefault(x.getZona().getNume(),0)+1); });
        return createCard("Populație Zoo", new ApexChartWrapper("donut", "Animale", new ArrayList<>(s.keySet()), new ArrayList<>(s.values()), new String[]{"#3b82f6", "#10b981", "#f59e0b"}));
    }

    private Component createHranaPerZonaChart() {
        List<Animal> a = repoAnimale.getAll();
        Map<String, Double> s = new HashMap<>();
        a.forEach(x -> { if(x.getZona()!=null) s.put(x.getZona().getNume(), s.getOrDefault(x.getZona().getNume(),0.0)+(x.getCantitateHrana()*x.getFrecventaHrana())); });
        return createCard("Consum Hrană Zilnic", new ApexChartWrapper("bar", "Kg/Zi", new ArrayList<>(s.keySet()), new ArrayList<>(s.values()), null));
    }

    private Component createCard(String title, Component content) {
        VerticalLayout c = new VerticalLayout(new Span(title), content);
        c.addClassNames(LumoUtility.Background.BASE, LumoUtility.BoxShadow.MEDIUM, LumoUtility.BorderRadius.LARGE, LumoUtility.Padding.MEDIUM);
        c.setWidth("450px");
        return c;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        raportService.close();
        repoAnimale.close();
        repoIngrijitori.close();
        super.onDetach(detachEvent);
    }
}