package web.views.bilete;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import entity.Bilet;
import repository.RepositoryBilete;
import web.MainLayout;
import web.PaginaPrincipala;
import com.vaadin.flow.component.DetachEvent;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Bilete")
@Route(value = "bilete", layout = MainLayout.class)
public class NavigareGridBileteView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryBilete repo = new RepositoryBilete();

    private List<Bilet> bilete = new ArrayList<>();
    private Bilet biletCurent;

    private final H1 titlu = new H1("Lista Bilete");
    private final TextField filterText = new TextField();
    private final Button cmdEditare = new Button("Editează bilet...");
    private final Button cmdAdaugare = new Button("Adaugă bilet...");
    private final Button cmdStergere = new Button("Șterge bilet");

    private final Grid<Bilet> grid = new Grid<>(Bilet.class, false);

    public NavigareGridBileteView() {
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel() {
        bilete = new ArrayList<>(repo.getAll());
        biletCurent = bilete.isEmpty() ? null : bilete.get(0);
        grid.setItems(bilete);
        if (biletCurent != null) grid.asSingleSelect().setValue(biletCurent);
    }

    private void initViewLayout() {
        filterText.setPlaceholder("Filtru după vizitator...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        HorizontalLayout toolbar = new HorizontalLayout(filterText, cmdEditare, cmdAdaugare, cmdStergere);

        grid.addColumn(Bilet::getId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(Bilet::getData).setHeader("Data").setAutoWidth(true);
        grid.addColumn(Bilet::getPret).setHeader("Preț").setAutoWidth(true);
        grid.addColumn(b -> b.getVizitator() != null ? b.getVizitator().getNume() : "N/A")
                .setHeader("Vizitator").setAutoWidth(true);

        grid.addComponentColumn(this::createGridActionsButtons).setHeader("Acțiuni").setAutoWidth(true);

        add(titlu, toolbar, grid);
    }

    private Component createGridActionsButtons(Bilet item) {
        Button edit = new Button("Edit");
        edit.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editBilet();
        });

        Button sterge = new Button("Șterge");
        sterge.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            stergeBilet();
            refreshForm();
        });

        return new HorizontalLayout(edit, sterge);
    }

    private void initControllerActions() {
        filterText.addValueChangeListener(e -> updateList());
        cmdEditare.addClickListener(e -> editBilet());
        cmdAdaugare.addClickListener(e -> adaugaBilet());
        cmdStergere.addClickListener(e -> {
            stergeBilet();
            refreshForm();
        });
    }

    private void updateList() {
        String f = filterText.getValue();
        if (f == null || f.isBlank()) {
            grid.setItems(bilete);
            return;
        }
        String ff = f.trim();
        grid.setItems(bilete.stream()
                .filter(b -> b.getVizitator() != null && b.getVizitator().getNume() != null
                        && b.getVizitator().getNume().contains(ff))
                .toList());
    }

    private void refreshForm() {
        bilete = new ArrayList<>(repo.getAll());
        grid.setItems(bilete);
        if (!bilete.isEmpty()) {
            if (biletCurent == null) biletCurent = bilete.get(0);
            grid.select(biletCurent);
        }
    }

    private void adaugaBilet() {
        getUI().ifPresent(ui -> ui.navigate(FormBiletView.class, 0));
    }

    private void editBilet() {
        biletCurent = grid.asSingleSelect().getValue();
        if (biletCurent != null) getUI().ifPresent(ui -> ui.navigate(FormBiletView.class, biletCurent.getId()));
    }

    private void stergeBilet() {
        biletCurent = grid.asSingleSelect().getValue();
        if (biletCurent != null) {
            repo.removeById(biletCurent.getId());
            biletCurent = null;
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null && id > 0) biletCurent = repo.getBiletDupaId(id);
        refreshForm();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repo.close();
        super.onDetach(detachEvent);
    }
}
