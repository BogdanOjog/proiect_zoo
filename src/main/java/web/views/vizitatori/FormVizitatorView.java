package web.views.vizitatori;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import entity.Vizitator;
import repository.RepositoryVizitatori;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

@PageTitle("Vizitator")
@Route(value = "vizitator", layout = MainLayout.class)
public class FormVizitatorView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryVizitatori repo = new RepositoryVizitatori();
    private Vizitator vizitator;

    private final H1 titlu = new H1("Form Vizitator");
    private final IntegerField id = new IntegerField("ID:");
    private final TextField nume = new TextField("Nume:");
    private final IntegerField varsta = new IntegerField("Vârstă:");

    private final Button cmdNou = new Button("Nou");
    private final Button cmdSterge = new Button("Șterge");
    private final Button cmdAbandon = new Button("Abandon");
    private final Button cmdSalveaza = new Button("Salvează");

    public FormVizitatorView() {
        initViewLayout();
        initControllerActions();
    }

    private void initViewLayout() {
        id.setReadOnly(true);
        FormLayout form = new FormLayout();
        form.add(id, nume, varsta);
        form.setMaxWidth("450px");

        add(titlu, form, new HorizontalLayout(cmdNou, cmdSterge, cmdAbandon, cmdSalveaza));
    }

    private void initControllerActions() {
        cmdNou.addClickListener(e -> {
            vizitator = new Vizitator();
            refreshForm();
        });

        cmdSterge.addClickListener(e -> {
            if (vizitator != null && vizitator.getId() > 0) repo.removeById(vizitator.getId());
            getUI().ifPresent(ui -> ui.navigate(NavigareGridVizitatoriView.class));
        });

        cmdAbandon.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(NavigareGridVizitatoriView.class)));

        cmdSalveaza.addClickListener(e -> {
            if (!citireDinForm()) return;

            try {
                if (vizitator.getId() <= 0) repo.add(vizitator);
                else repo.update(vizitator);

                Notification.show("Vizitator salvat");
                getUI().ifPresent(ui -> ui.navigate(NavigareGridVizitatoriView.class, vizitator.getId()));
            } catch (Exception ex) {
                Notification.show("Eroare: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });
    }

    private boolean citireDinForm() {
        if (vizitator == null) vizitator = new Vizitator();

        String n = nume.getValue();
        Integer v = varsta.getValue();

        if (n == null || n.isBlank()) {
            Notification.show("Numele este obligatoriu");
            return false;
        }
        if (v == null || v < 0) {
            Notification.show("Vârsta trebuie să fie >= 0");
            return false;
        }

        vizitator.setNume(n.trim());
        vizitator.setVarsta(v);
        return true;
    }

    private void refreshForm() {
        if (vizitator == null) vizitator = new Vizitator();
        id.setValue(vizitator.getId());
        nume.setValue(vizitator.getNume() == null ? "" : vizitator.getNume());
        varsta.setValue(vizitator.getVarsta());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer idParam) {
        if (idParam != null && idParam > 0) {
            vizitator = repo.getVizitatorDupaId(idParam);
            if (vizitator == null) vizitator = new Vizitator();
        } else {
            vizitator = new Vizitator();
        }
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repo.close();
        super.onDetach(detachEvent);
    }
}
