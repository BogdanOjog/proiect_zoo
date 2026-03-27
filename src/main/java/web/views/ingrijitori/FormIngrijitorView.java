package web.views.ingrijitori;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import entity.Ingrijitor;
import entity.Zona;
import repository.RepositoryIngrijitori;
import repository.RepositoryZone;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

import java.util.List;

@PageTitle("Îngrijitor")
@Route(value = "ingrijitor", layout = MainLayout.class)
public class FormIngrijitorView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryIngrijitori repoIngrijitori = new RepositoryIngrijitori();
    private final RepositoryZone repoZone = new RepositoryZone();

    private Ingrijitor ingrijitor;

    private final H1 titlu = new H1("Form Îngrijitor");

    private final IntegerField id = new IntegerField("ID:");
    private final TextField nume = new TextField("Nume:");
    private final IntegerField varsta = new IntegerField("Vârstă:");
    private final ComboBox<Zona> zonaAsignata = new ComboBox<>("Zonă asignată:");
    private final NumberField tarifOrar = new NumberField("Tarif/oră:");

    private final Button cmdNou = new Button("Nou");
    private final Button cmdSterge = new Button("Șterge");
    private final Button cmdAbandon = new Button("Abandon");
    private final Button cmdSalveaza = new Button("Salvează");

    public FormIngrijitorView() {
        initViewLayout();
        initControllerActions();
    }

    private void initViewLayout() {
        id.setReadOnly(true);

        List<Zona> zone = repoZone.getAll();
        zonaAsignata.setItems(zone);
        zonaAsignata.setItemLabelGenerator(z -> z.getId() + " - " + z.getNume());

        tarifOrar.setMin(0);

        FormLayout form = new FormLayout();
        form.add(id, nume, varsta, zonaAsignata, tarifOrar);
        form.setMaxWidth("520px");

        add(titlu, form, new HorizontalLayout(cmdNou, cmdSterge, cmdAbandon, cmdSalveaza));
    }

    private void initControllerActions() {
        cmdNou.addClickListener(e -> {
            ingrijitor = new Ingrijitor();
            refreshForm();
        });

        cmdSterge.addClickListener(e -> {
            if (ingrijitor != null && ingrijitor.getId() > 0) repoIngrijitori.removeById(ingrijitor.getId());
            getUI().ifPresent(ui -> ui.navigate(NavigareGridIngrijitoriView.class));
        });

        cmdAbandon.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(NavigareGridIngrijitoriView.class)));

        cmdSalveaza.addClickListener(e -> {
            if (!citireDinForm()) return;
            try {
                if (ingrijitor.getId() <= 0) repoIngrijitori.add(ingrijitor);
                else repoIngrijitori.update(ingrijitor);

                Notification.show("Îngrijitor salvat");
                getUI().ifPresent(ui -> ui.navigate(NavigareGridIngrijitoriView.class, ingrijitor.getId()));
            } catch (Exception ex) {
                Notification.show("Eroare: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });
    }

    private boolean citireDinForm() {
        if (ingrijitor == null) ingrijitor = new Ingrijitor();

        String n = nume.getValue();
        Integer v = varsta.getValue();
        Zona z = zonaAsignata.getValue();
        Double t = tarifOrar.getValue();

        if (n == null || n.isBlank()) { Notification.show("Numele este obligatoriu"); return false; }
        if (v == null || v < 0) { Notification.show("Vârsta trebuie să fie >= 0"); return false; }
        if (z == null) { Notification.show("Zona asignată este obligatorie"); return false; }
        if (t == null || t < 0) { Notification.show("Tariful/oră trebuie să fie >= 0"); return false; }

        ingrijitor.setNume(n.trim());
        ingrijitor.setVarsta(v);
        ingrijitor.setZonaAsignata(z);
        ingrijitor.setTarifOrar(t);

        return true;
    }

    private void refreshForm() {
        if (ingrijitor == null) ingrijitor = new Ingrijitor();

        id.setValue(ingrijitor.getId());
        nume.setValue(ingrijitor.getNume() == null ? "" : ingrijitor.getNume());
        varsta.setValue(ingrijitor.getVarsta());
        zonaAsignata.setValue(ingrijitor.getZonaAsignata());
        tarifOrar.setValue(ingrijitor.getTarifOrar());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer idParam) {
        if (idParam != null && idParam > 0) {
            ingrijitor = repoIngrijitori.getIngrijitorDupaId(idParam);
            if (ingrijitor == null) ingrijitor = new Ingrijitor();
        } else {
            ingrijitor = new Ingrijitor();
        }
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repoIngrijitori.close();
        repoZone.close();
        super.onDetach(detachEvent);
    }
}
