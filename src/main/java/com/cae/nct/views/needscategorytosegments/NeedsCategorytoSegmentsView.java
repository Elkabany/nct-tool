package com.cae.nct.views.needscategorytosegments;

import com.cae.nct.data.entity.NEEDCAT_SEG;
import com.cae.nct.data.service.NEEDCAT_SEGService;
import com.cae.nct.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Needs Category to Segments")
@Route(value = "NeedCatSeg/:nEEDCAT_SEGID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class NeedsCategorytoSegmentsView extends Div implements BeforeEnterObserver {

    private final String NEEDCAT_SEG_ID = "nEEDCAT_SEGID";
    private final String NEEDCAT_SEG_EDIT_ROUTE_TEMPLATE = "NeedCatSeg/%s/edit";

    private final Grid<NEEDCAT_SEG> grid = new Grid<>(NEEDCAT_SEG.class, false);

    private TextField category;
    private TextField segment;
    private TextField categortExtId;
    private TextField segmentExtId;
    private Checkbox active;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<NEEDCAT_SEG> binder;

    private NEEDCAT_SEG nEEDCAT_SEG;

    private final NEEDCAT_SEGService nEEDCAT_SEGService;

    @Autowired
    public NeedsCategorytoSegmentsView(NEEDCAT_SEGService nEEDCAT_SEGService) {
        this.nEEDCAT_SEGService = nEEDCAT_SEGService;
        addClassNames("needs-categoryto-segments-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("category").setAutoWidth(true);
        grid.addColumn("segment").setAutoWidth(true);
        grid.addColumn("categortExtId").setAutoWidth(true);
        grid.addColumn("segmentExtId").setAutoWidth(true);
        LitRenderer<NEEDCAT_SEG> activeRenderer = LitRenderer.<NEEDCAT_SEG>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.setItems(query -> nEEDCAT_SEGService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(NEEDCAT_SEG_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(NeedsCategorytoSegmentsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(NEEDCAT_SEG.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(categortExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("categortExtId");
        binder.forField(segmentExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("segmentExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.nEEDCAT_SEG == null) {
                    this.nEEDCAT_SEG = new NEEDCAT_SEG();
                }
                binder.writeBean(this.nEEDCAT_SEG);
                nEEDCAT_SEGService.update(this.nEEDCAT_SEG);
                clearForm();
                refreshGrid();
                Notification.show("NEEDCAT_SEG details stored.");
                UI.getCurrent().navigate(NeedsCategorytoSegmentsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the nEEDCAT_SEG details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> nEEDCAT_SEGId = event.getRouteParameters().get(NEEDCAT_SEG_ID).map(UUID::fromString);
        if (nEEDCAT_SEGId.isPresent()) {
            Optional<NEEDCAT_SEG> nEEDCAT_SEGFromBackend = nEEDCAT_SEGService.get(nEEDCAT_SEGId.get());
            if (nEEDCAT_SEGFromBackend.isPresent()) {
                populateForm(nEEDCAT_SEGFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested nEEDCAT_SEG was not found, ID = %s", nEEDCAT_SEGId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(NeedsCategorytoSegmentsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        category = new TextField("Category");
        segment = new TextField("Segment");
        categortExtId = new TextField("Categort Ext Id");
        segmentExtId = new TextField("Segment Ext Id");
        active = new Checkbox("Active");
        formLayout.add(category, segment, categortExtId, segmentExtId, active);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(NEEDCAT_SEG value) {
        this.nEEDCAT_SEG = value;
        binder.readBean(this.nEEDCAT_SEG);

    }
}
