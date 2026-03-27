package web.views.ingrijitori;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import entity.Ingrijitor;
import repository.RepositoryIngrijitori;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Îngrijitori")
@Route(value = "ingrijitori", layout = MainLayout.class)
public class NavigareGridIngrijitoriView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryIngrijitori repo = new RepositoryIngrijitori();

    private List<Ingrijitor> ingrijitori = new ArrayList<>();
    private Ingrijitor ingrijitorCurent;

    private final H1 titlu = new H1("Lista Îngrijitori");
    private final TextField filterText = new TextField();
    private final Button cmdEditare = new Button("Editează îngrijitor...");
    private final Button cmdAdaugare = new Button("Adaugă îngrijitor...");
    private final Button cmdStergere = new Button("Șterge îngrijitor");

    private final Grid<Ingrijitor> grid = new Grid<>(Ingrijitor.class, false);

    public NavigareGridIngrijitoriView() {
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel() {
        ingrijitori = new ArrayList<>(repo.getAll());
        ingrijitorCurent = ingrijitori.isEmpty() ? null : ingrijitori.get(0);
        grid.setItems(ingrijitori);
        if (ingrijitorCurent != null) grid.asSingleSelect().setValue(ingrijitorCurent);
    }

    private void initViewLayout() {
        filterText.setPlaceholder("Filtru după nume...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        HorizontalLayout toolbar = new HorizontalLayout(filterText, cmdEditare, cmdAdaugare, cmdStergere);

        grid.addColumn(Ingrijitor::getId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(Ingrijitor::getNume).setHeader("Nume").setAutoWidth(true);
        grid.addColumn(Ingrijitor::getVarsta).setHeader("Vârstă").setAutoWidth(true);
        grid.addColumn(i -> i.getZonaAsignata() != null ? i.getZonaAsignata().getNume() : "N/A")
                .setHeader("Zonă asignată").setAutoWidth(true);
        grid.addColumn(Ingrijitor::getTarifOrar).setHeader("Tarif/oră").setAutoWidth(true);

        grid.addComponentColumn(this::createGridActionsButtons).setHeader("Acțiuni").setAutoWidth(true);

        add(titlu, toolbar, grid);
    }

    private Component createGridActionsButtons(Ingrijitor item) {
        Button edit = new Button("Edit");
        edit.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editIngrijitor();
        });

        Button sterge = new Button("Șterge");
        sterge.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            stergeIngrijitor();
            refreshForm();
        });

        return new HorizontalLayout(edit, sterge);
    }

    private void initControllerActions() {
        filterText.addValueChangeListener(e -> updateList());
        cmdEditare.addClickListener(e -> editIngrijitor());
        cmdAdaugare.addClickListener(e -> adaugaIngrijitor());
        cmdStergere.addClickListener(e -> {
            stergeIngrijitor();
            refreshForm();
        });
    }

    private void updateList() {
        String f = filterText.getValue();
        if (f == null || f.isBlank()) {
            grid.setItems(ingrijitori);
            return;
        }
        String ff = f.trim();
        grid.setItems(ingrijitori.stream()
                .filter(i -> i.getNume() != null && i.getNume().contains(ff))
                .toList());
    }

    private void refreshForm() {
        ingrijitori = new ArrayList<>(repo.getAll());
        grid.setItems(ingrijitori);
        if (!ingrijitori.isEmpty()) {
            if (ingrijitorCurent == null) ingrijitorCurent = ingrijitori.get(0);
            grid.select(ingrijitorCurent);
        }
    }

    private void adaugaIngrijitor() {
        getUI().ifPresent(ui -> ui.navigate(FormIngrijitorView.class, 0));
    }

    private void editIngrijitor() {
        ingrijitorCurent = grid.asSingleSelect().getValue();
        if (ingrijitorCurent != null) {
            getUI().ifPresent(ui -> ui.navigate(FormIngrijitorView.class, ingrijitorCurent.getId()));
        }
    }

    private void stergeIngrijitor() {
        ingrijitorCurent = grid.asSingleSelect().getValue();
        if (ingrijitorCurent != null) {
            repo.removeById(ingrijitorCurent.getId());
            ingrijitorCurent = null;
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null && id > 0) ingrijitorCurent = repo.getIngrijitorDupaId(id);
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repo.close();
        super.onDetach(detachEvent);
    }
}
