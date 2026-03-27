package web.views.zone;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import entity.Zona;
import repository.RepositoryZone;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Zone")
@Route(value = "zone", layout = MainLayout.class)
public class NavigareGridZoneView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryZone repo = new RepositoryZone();

    private List<Zona> zone = new ArrayList<>();
    private Zona zonaCurenta;

    private final H1 titlu = new H1("Lista Zone");
    private final TextField filterText = new TextField();
    private final Button cmdEditare = new Button("Editează zona...");
    private final Button cmdAdaugare = new Button("Adaugă zonă...");
    private final Button cmdStergere = new Button("Șterge zonă");

    private final Grid<Zona> grid = new Grid<>(Zona.class, false);

    public NavigareGridZoneView() {
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel() {
        zone = new ArrayList<>(repo.getAll());
        zonaCurenta = zone.isEmpty() ? null : zone.get(0);
        grid.setItems(zone);
        if (zonaCurenta != null) grid.asSingleSelect().setValue(zonaCurenta);
    }

    private void initViewLayout() {
        filterText.setPlaceholder("Filtru după nume...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        HorizontalLayout toolbar = new HorizontalLayout(filterText, cmdEditare, cmdAdaugare, cmdStergere);

        grid.addColumn(Zona::getId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(Zona::getNume).setHeader("Nume").setAutoWidth(true);
        grid.addColumn(Zona::getCapacitateMaxima).setHeader("Capacitate maximă").setAutoWidth(true);
        grid.addComponentColumn(this::createGridActionsButtons).setHeader("Acțiuni").setAutoWidth(true);

        add(titlu, toolbar, grid);
    }

    private Component createGridActionsButtons(Zona item) {
        Button edit = new Button("Edit");
        edit.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editZona();
        });

        Button sterge = new Button("Șterge");
        sterge.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            stergeZona();
            refreshForm();
        });

        return new HorizontalLayout(edit, sterge);
    }

    private void initControllerActions() {
        filterText.addValueChangeListener(e -> updateList());
        cmdEditare.addClickListener(e -> editZona());
        cmdAdaugare.addClickListener(e -> adaugaZona());
        cmdStergere.addClickListener(e -> {
            stergeZona();
            refreshForm();
        });
    }

    private void updateList() {
        String f = filterText.getValue();
        if (f == null || f.isBlank()) {
            grid.setItems(zone);
            return;
        }
        String ff = f.trim();
        grid.setItems(zone.stream().filter(z -> z.getNume() != null && z.getNume().contains(ff)).toList());
    }

    private void refreshForm() {
        zone = new ArrayList<>(repo.getAll());
        grid.setItems(zone);
        if (!zone.isEmpty()) {
            if (zonaCurenta == null) zonaCurenta = zone.get(0);
            grid.select(zonaCurenta);
        }
    }

    private void adaugaZona() {
        getUI().ifPresent(ui -> ui.navigate(FormZonaView.class, 0));
    }

    private void editZona() {
        zonaCurenta = grid.asSingleSelect().getValue();
        if (zonaCurenta != null) {
            int id = zonaCurenta.getId();
            getUI().ifPresent(ui -> ui.navigate(FormZonaView.class, id));
        }
    }

    private void stergeZona() {
        zonaCurenta = grid.asSingleSelect().getValue();
        if (zonaCurenta != null) {
            repo.removeById(zonaCurenta.getId());
            zonaCurenta = null;
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null && id > 0) {
            zonaCurenta = repo.getZonaDupaId(id);
        }
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repo.close();
        super.onDetach(detachEvent);
    }
}
