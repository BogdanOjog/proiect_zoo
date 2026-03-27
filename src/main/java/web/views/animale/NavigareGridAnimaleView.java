package web.views.animale;

import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import entity.Animal;
import repository.RepositoryAnimale;
import web.MainLayout;
import com.vaadin.flow.component.DetachEvent;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Animale")
@Route(value = "animale", layout = MainLayout.class)
public class NavigareGridAnimaleView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RepositoryAnimale repo = new RepositoryAnimale();
    private List<Animal> animale = new ArrayList<>();
    private Animal animalCurent;


    private final TextField filterText = new TextField();
    private final Grid<Animal> grid = new Grid<>(Animal.class, false);

    public NavigareGridAnimaleView() {
        addClassName("list-view");
        setSizeFull();

        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel() {
        animale = new ArrayList<>(repo.getAll());
        grid.setItems(animale);
    }

    private void initViewLayout() {
        HorizontalLayout toolbar = createToolbar();

        configureGrid();

        add(new H2("Gestiune Animale"), toolbar, grid);
    }

    private HorizontalLayout createToolbar() {
        filterText.setPlaceholder("Caută după nume...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());

        Button addContactButton = new Button("Adaugă Animal");
        addContactButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addContactButton.addClickListener(e -> adaugaAnimal());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(new ComponentRenderer<>(animal -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(Alignment.CENTER);

            Avatar avatar = new Avatar(animal.getNume());

            avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);

            Span name = new Span(animal.getNume());
            name.addClassNames(LumoUtility.FontWeight.BOLD);

            row.add(avatar, name);
            return row;
        })).setHeader("Nume").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(animal -> {
            Span badge = new Span(animal.getTipAnimal());
            badge.getElement().getThemeList().add("badge");

            if ("Leu".equals(animal.getTipAnimal())) {
                badge.getElement().getThemeList().add("error");
            } else if ("Zebra".equals(animal.getTipAnimal())) {
                badge.getElement().getThemeList().add("success");
            }
            return badge;
        })).setHeader("Tip").setAutoWidth(true);

        grid.addColumn(Animal::getSex).setHeader("Sex").setWidth("80px").setFlexGrow(0);
        grid.addColumn(Animal::getVarsta).setHeader("Vârstă").setWidth("100px").setFlexGrow(0);


        grid.addColumn(new ComponentRenderer<>(animal -> {
            String zonaNume = (animal.getZona() != null) ? animal.getZona().getNume() : "Fără Zonă";
            Span badge = new Span(zonaNume);
            badge.getElement().getThemeList().add("badge contrast");
            return badge;
        })).setHeader("Zonă").setAutoWidth(true);

        grid.addComponentColumn(animal -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create());
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editBtn.addClickListener(e -> {
                editAnimal(animal);
            });

            Button deleteBtn = new Button(VaadinIcon.TRASH.create());
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> {
                repo.removeById(animal.getId());
                refreshGrid();
            });

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Acțiuni");
    }

    private void initControllerActions() {
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList() {
        String f = filterText.getValue();
        if (f == null || f.isBlank()) {
            grid.setItems(animale);
        } else {
            grid.setItems(animale.stream()
                    .filter(a -> a.getNume() != null && a.getNume().toLowerCase().contains(f.toLowerCase()))
                    .toList());
        }
    }

    private void refreshGrid() {
        animale = new ArrayList<>(repo.getAll());
        grid.setItems(animale);
    }

    private void adaugaAnimal() {
        getUI().ifPresent(ui -> ui.navigate(FormAnimalView.class, 0));
    }

    private void editAnimal(Animal animal) {
        if (animal != null) {
            getUI().ifPresent(ui -> ui.navigate(FormAnimalView.class, animal.getId()));
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        refreshGrid();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        repo.close();
        super.onDetach(detachEvent);
    }
}