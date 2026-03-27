package web.views.zone;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import entity.Zona;
import repository.RepositoryZone;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

@PageTitle("Zonă")
@Route(value = "zonă", layout = MainLayout.class)
public class FormZonaView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryZone repo = new RepositoryZone();

    private Zona zona;

    private final H1 titlu = new H1("Form Zonă");
    private final IntegerField id = new IntegerField("ID zonă:");
    private final TextField nume = new TextField("Nume:");
    private final IntegerField capacitateMaxima = new IntegerField("Capacitate maximă:");

    private final Button cmdNou = new Button("Nou");
    private final Button cmdSterge = new Button("Șterge");
    private final Button cmdAbandon = new Button("Abandon");
    private final Button cmdSalveaza = new Button("Salvează");

    public FormZonaView() {
        initViewLayout();
        initControllerActions();
    }

    private void initViewLayout() {
        id.setReadOnly(true);

        FormLayout form = new FormLayout();
        form.add(id, nume, capacitateMaxima);
        form.setMaxWidth("450px");

        HorizontalLayout toolbar = new HorizontalLayout(cmdNou, cmdSterge, cmdAbandon, cmdSalveaza);

        add(titlu, form, toolbar);
    }

    private void initControllerActions() {
        cmdNou.addClickListener(e -> {
            zona = new Zona();
            refreshForm();
        });

        cmdSterge.addClickListener(e -> {
            if (zona != null && zona.getId() > 0) {
                repo.removeById(zona.getId());
                Notification.show("Zonă ștearsă");
            }
            getUI().ifPresent(ui -> ui.navigate(NavigareGridZoneView.class));
        });

        cmdAbandon.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(NavigareGridZoneView.class)));

        cmdSalveaza.addClickListener(e -> {
            if (!citireDinForm()) return;

            try {
                if (zona.getId() <= 0) {
                    repo.add(zona);
                } else {
                    repo.update(zona);
                }
                Notification.show("Zonă salvată");
                int idNou = zona.getId();
                getUI().ifPresent(ui -> ui.navigate(NavigareGridZoneView.class, idNou));
            } catch (Exception ex) {
                Notification.show("Eroare la salvare: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });
    }

    private boolean citireDinForm() {
        String n = nume.getValue();
        Integer cap = capacitateMaxima.getValue();

        if (n == null || n.isBlank()) {
            Notification.show("Numele zonei este obligatoriu");
            return false;
        }
        if (cap == null || cap < 0) {
            Notification.show("Capacitatea maximă trebuie să fie >= 0");
            return false;
        }

        zona.setNume(n.trim());
        zona.setCapacitateMaxima(cap);
        return true;
    }

    private void refreshForm() {
        if (zona == null) zona = new Zona();
        id.setValue(zona.getId());
        nume.setValue(zona.getNume() == null ? "" : zona.getNume());
        capacitateMaxima.setValue(zona.getCapacitateMaxima());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer idParam) {
        if (idParam != null && idParam > 0) {
            zona = repo.getZonaDupaId(idParam);
            if (zona == null) zona = new Zona();
        } else {
            zona = new Zona();
        }
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repo.close();
        super.onDetach(detachEvent);
    }
}
