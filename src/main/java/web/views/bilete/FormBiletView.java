package web.views.bilete;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.*;
import entity.Bilet;
import entity.Vizitator;
import repository.RepositoryBilete;
import repository.RepositoryVizitatori;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

import java.time.LocalDate;
import java.util.List;

@PageTitle("Bilet")
@Route(value = "bilet", layout = MainLayout.class)
public class FormBiletView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryBilete repoBilete = new RepositoryBilete();
    private final RepositoryVizitatori repoVizitatori = new RepositoryVizitatori();

    private Bilet bilet;

    private final H1 titlu = new H1("Form Bilet");

    private final IntegerField id = new IntegerField("ID:");
    private final NumberField pret = new NumberField("Preț:");
    private final DatePicker data = new DatePicker("Data:");
    private final ComboBox<Vizitator> vizitator = new ComboBox<>("Vizitator:");

    private final Button cmdNou = new Button("Nou");
    private final Button cmdSterge = new Button("Șterge");
    private final Button cmdAbandon = new Button("Abandon");
    private final Button cmdSalveaza = new Button("Salvează");

    public FormBiletView() {
        initViewLayout();
        initControllerActions();
    }

    private void initViewLayout() {
        id.setReadOnly(true);
        pret.setMin(0);

        List<Vizitator> vizitatori = repoVizitatori.getAll();
        vizitator.setItems(vizitatori);
        vizitator.setItemLabelGenerator(v -> v.getId() + " - " + v.getNume());

        FormLayout form = new FormLayout();
        form.add(id, pret, data, vizitator);
        form.setMaxWidth("520px");

        add(titlu, form, new HorizontalLayout(cmdNou, cmdSterge, cmdAbandon, cmdSalveaza));
    }

    private void initControllerActions() {
        cmdNou.addClickListener(e -> {
            bilet = new Bilet();
            refreshForm();
        });

        cmdSterge.addClickListener(e -> {
            if (bilet != null && bilet.getId() > 0) repoBilete.removeById(bilet.getId());
            getUI().ifPresent(ui -> ui.navigate(NavigareGridBileteView.class));
        });

        cmdAbandon.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(NavigareGridBileteView.class)));

        cmdSalveaza.addClickListener(e -> {
            if (!citireDinForm()) return;
            try {
                if (bilet.getId() <= 0) repoBilete.add(bilet);
                else repoBilete.update(bilet);

                Notification.show("Bilet salvat");
                getUI().ifPresent(ui -> ui.navigate(NavigareGridBileteView.class, bilet.getId()));
            } catch (Exception ex) {
                Notification.show("Eroare: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });
    }

    private boolean citireDinForm() {
        if (bilet == null) bilet = new Bilet();

        Double p = pret.getValue();
        LocalDate d = data.getValue();
        Vizitator v = vizitator.getValue();

        if (p == null || p < 0) { Notification.show("Preț invalid"); return false; }
        if (d == null) { Notification.show("Data este obligatorie"); return false; }
        if (v == null) { Notification.show("Vizitatorul este obligatoriu"); return false; }

        bilet.setPret(p);
        bilet.setData(d);
        bilet.setVizitator(v);

        return true;
    }

    private void refreshForm() {
        if (bilet == null) bilet = new Bilet();

        id.setValue(bilet.getId());
        pret.setValue(bilet.getPret());
        data.setValue(bilet.getData());
        vizitator.setValue(bilet.getVizitator());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer idParam) {
        if (idParam != null && idParam > 0) {
            bilet = repoBilete.getBiletDupaId(idParam);
            if (bilet == null) bilet = new Bilet();
        } else {
            bilet = new Bilet();
        }
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repoBilete.close();
        repoVizitatori.close();
        super.onDetach(detachEvent);
    }
}
