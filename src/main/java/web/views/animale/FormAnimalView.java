package web.views.animale;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import entity.*;
import repository.RepositoryAnimale;
import repository.RepositoryZone;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

import java.util.List;

@PageTitle("Animal")
@Route(value = "animal", layout = MainLayout.class)
public class FormAnimalView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryAnimale repoAnimale = new RepositoryAnimale();
    private final RepositoryZone repoZone = new RepositoryZone();

    private Animal animal;

    private final H1 titlu = new H1("Form Animal");

    private final IntegerField id = new IntegerField("ID:");
    private final ComboBox<String> tip = new ComboBox<>("Tip animal:");
    private final TextField nume = new TextField("Nume:");
    private final ComboBox<String> sex = new ComboBox<>("Sex:");
    private final IntegerField varsta = new IntegerField("Vârstă:");
    private final NumberField inaltime = new NumberField("Înălțime:");
    private final NumberField greutate = new NumberField("Greutate:");
    private final NumberField cantitateHrana = new NumberField("Cantitate hrană:");
    private final NumberField cantitateApa = new NumberField("Cantitate apă:");
    private final NumberField frecventaHrana = new NumberField("Frecvență hrană:");
    private final ComboBox<Zona> zona = new ComboBox<>("Zonă:");

    private final Button cmdNou = new Button("Nou");
    private final Button cmdSterge = new Button("Șterge");
    private final Button cmdAbandon = new Button("Abandon");
    private final Button cmdSalveaza = new Button("Salvează");

    public FormAnimalView() {
        initViewLayout();
        initControllerActions();
    }

    private void initViewLayout() {
        id.setReadOnly(true);

        tip.setItems("Mamifer", "Pasare", "Peste");
        sex.setItems("M", "F");

        List<Zona> zone = repoZone.getAll();
        zona.setItems(zone);
        zona.setItemLabelGenerator(z -> z.getId() + " - " + z.getNume());

        // minime
        varsta.setMin(0);
        inaltime.setMin(0);
        greutate.setMin(0);
        cantitateHrana.setMin(0);
        cantitateApa.setMin(0);
        frecventaHrana.setMin(0);

        FormLayout form = new FormLayout();
        form.add(id, tip, nume, sex, varsta, inaltime, greutate, cantitateHrana, cantitateApa, frecventaHrana, zona);
        form.setMaxWidth("650px");

        add(titlu, form, new HorizontalLayout(cmdNou, cmdSterge, cmdAbandon, cmdSalveaza));
    }

    private void initControllerActions() {
        cmdNou.addClickListener(e -> {
            animal = null;
            clearForm();
        });

        cmdSterge.addClickListener(e -> {
            if (animal != null && animal.getId() > 0) repoAnimale.removeById(animal.getId());
            getUI().ifPresent(ui -> ui.navigate(NavigareGridAnimaleView.class));
        });

        cmdAbandon.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(NavigareGridAnimaleView.class)));

        cmdSalveaza.addClickListener(e -> {
            if (!citireDinForm()) return;

            try {
                if (animal.getId() <= 0) repoAnimale.add(animal);
                else repoAnimale.update(animal);

                Notification.show("Animal salvat");
                getUI().ifPresent(ui -> ui.navigate(NavigareGridAnimaleView.class, animal.getId()));
            } catch (Exception ex) {
                Notification.show("Eroare: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });
    }

    private void clearForm() {
        id.setValue(0);
        tip.clear();
        nume.clear();
        sex.clear();
        varsta.clear();
        inaltime.clear();
        greutate.clear();
        cantitateHrana.clear();
        cantitateApa.clear();
        frecventaHrana.clear();
        zona.clear();
    }

    private boolean citireDinForm() {
        String tipSelectat = tip.getValue();
        String n = nume.getValue();
        String s = sex.getValue();
        Integer v = varsta.getValue();
        Double in = inaltime.getValue();
        Double gr = greutate.getValue();
        Double ch = cantitateHrana.getValue();
        Double ca = cantitateApa.getValue();
        Double fh = frecventaHrana.getValue();
        Zona z = zona.getValue();

        if (tipSelectat == null) { Notification.show("Tipul animalului este obligatoriu"); return false; }
        if (n == null || n.isBlank()) { Notification.show("Numele este obligatoriu"); return false; }
        if (s == null) { Notification.show("Sexul este obligatoriu"); return false; }
        if (v == null || v < 0) { Notification.show("Vârsta trebuie să fie >= 0"); return false; }
        if (z == null) { Notification.show("Zona este obligatorie"); return false; }

        if (animal == null) {
            animal = switch (tipSelectat) {
                case "Mamifer" -> new Mamifer();
                case "Pasare" -> new Pasare();
                case "Peste" -> new Peste();
                default -> new Mamifer();
            };
        }

        animal.setNume(n.trim());
        animal.setSex(s);
        animal.setVarsta(v);
        animal.setInaltime(in == null ? 0 : in);
        animal.setGreutate(gr == null ? 0 : gr);
        animal.setCantitateHrana(ch == null ? 0 : ch);
        animal.setCantitateApa(ca == null ? 0 : ca);
        animal.setFrecventaHrana(fh == null ? 0 : fh);
        animal.setZona(z);

        return true;
    }

    private void refreshForm() {
        if (animal == null) {
            clearForm();
            return;
        }

        id.setValue(animal.getId());
        tip.setValue(animal.getTipAnimal()); // discriminator
        nume.setValue(animal.getNume() == null ? "" : animal.getNume());
        sex.setValue(animal.getSex());
        varsta.setValue(animal.getVarsta());
        inaltime.setValue(animal.getInaltime());
        greutate.setValue(animal.getGreutate());
        cantitateHrana.setValue(animal.getCantitateHrana());
        cantitateApa.setValue(animal.getCantitateApa());
        frecventaHrana.setValue(animal.getFrecventaHrana());
        zona.setValue(animal.getZona());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer idParam) {
        if (idParam != null && idParam > 0) {
            animal = repoAnimale.getAnimalDupaId(idParam);
        } else {
            animal = null;
        }
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repoAnimale.close();
        repoZone.close();
        super.onDetach(detachEvent);
    }
}
