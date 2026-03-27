package web.views.vizitatori;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import entity.Vizitator;
import repository.RepositoryVizitatori;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Vizitatori")
@Route(value = "vizitatori", layout = MainLayout.class)
public class NavigareGridVizitatoriView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryVizitatori repo = new RepositoryVizitatori();

    private List<Vizitator> vizitatori = new ArrayList<>();
    private Vizitator vizitatorCurent;

    private final H1 titlu = new H1("Lista Vizitatori");
    private final TextField filterText = new TextField();
    private final Button cmdEditare = new Button("Editează vizitator...");
    private final Button cmdAdaugare = new Button("Adaugă vizitator...");
    private final Button cmdStergere = new Button("Șterge vizitator");

    private final Grid<Vizitator> grid = new Grid<>(Vizitator.class, false);

    public NavigareGridVizitatoriView() {
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel() {
        vizitatori = new ArrayList<>(repo.getAll());
        vizitatorCurent = vizitatori.isEmpty() ? null : vizitatori.get(0);
        grid.setItems(vizitatori);
        if (vizitatorCurent != null) grid.asSingleSelect().setValue(vizitatorCurent);
    }

    private void initViewLayout() {
        filterText.setPlaceholder("Filtru după nume...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        HorizontalLayout toolbar = new HorizontalLayout(filterText, cmdEditare, cmdAdaugare, cmdStergere);

        grid.addColumn(Vizitator::getId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(Vizitator::getNume).setHeader("Nume").setAutoWidth(true);
        grid.addColumn(Vizitator::getVarsta).setHeader("Vârstă").setAutoWidth(true);
        grid.addComponentColumn(this::createGridActionsButtons).setHeader("Acțiuni").setAutoWidth(true);

        add(titlu, toolbar, grid);
    }

    private Component createGridActionsButtons(Vizitator item) {
        Button edit = new Button("Edit");
        edit.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editVizitator();
        });

        Button sterge = new Button("Șterge");
        sterge.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            stergeVizitator();
            refreshForm();
        });

        return new HorizontalLayout(edit, sterge);
    }

    private void initControllerActions() {
        filterText.addValueChangeListener(e -> updateList());
        cmdEditare.addClickListener(e -> editVizitator());
        cmdAdaugare.addClickListener(e -> adaugaVizitator());
        cmdStergere.addClickListener(e -> {
            stergeVizitator();
            refreshForm();
        });
    }

    private void updateList() {
        String f = filterText.getValue();
        if (f == null || f.isBlank()) {
            grid.setItems(vizitatori);
            return;
        }
        String ff = f.trim();
        grid.setItems(vizitatori.stream()
                .filter(v -> v.getNume() != null && v.getNume().contains(ff))
                .toList());
    }

    private void refreshForm() {
        vizitatori = new ArrayList<>(repo.getAll());
        grid.setItems(vizitatori);
        if (!vizitatori.isEmpty()) {
            if (vizitatorCurent == null) vizitatorCurent = vizitatori.get(0);
            grid.select(vizitatorCurent);
        }
    }

    private void adaugaVizitator() {
        getUI().ifPresent(ui -> ui.navigate(FormVizitatorView.class, 0));
    }

    private void editVizitator() {
        vizitatorCurent = grid.asSingleSelect().getValue();
        if (vizitatorCurent != null) {
            getUI().ifPresent(ui -> ui.navigate(FormVizitatorView.class, vizitatorCurent.getId()));
        }
    }

    private void stergeVizitator() {
        vizitatorCurent = grid.asSingleSelect().getValue();
        if (vizitatorCurent != null) {
            repo.removeById(vizitatorCurent.getId());
            vizitatorCurent = null;
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null && id > 0) {
            vizitatorCurent = repo.getVizitatorDupaId(id);
        }
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repo.close();
        super.onDetach(detachEvent);
    }
}
