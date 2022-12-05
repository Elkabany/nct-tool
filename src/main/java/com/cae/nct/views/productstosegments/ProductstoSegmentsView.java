package com.cae.nct.views.productstosegments;

import com.cae.nct.data.entity.PROD_SEG;
import com.cae.nct.data.service.PROD_SEGService;
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

@PageTitle("Products to Segments")
@Route(value = "ProductSegment/:pROD_SEGID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class ProductstoSegmentsView extends Div implements BeforeEnterObserver {

    private final String PROD_SEG_ID = "pROD_SEGID";
    private final String PROD_SEG_EDIT_ROUTE_TEMPLATE = "ProductSegment/%s/edit";

    private final Grid<PROD_SEG> grid = new Grid<>(PROD_SEG.class, false);

    private TextField productExtId;
    private TextField segmentExtId;
    private TextField productName;
    private TextField segment;
    private Checkbox active;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PROD_SEG> binder;

    private PROD_SEG pROD_SEG;

    private final PROD_SEGService pROD_SEGService;

    @Autowired
    public ProductstoSegmentsView(PROD_SEGService pROD_SEGService) {
        this.pROD_SEGService = pROD_SEGService;
        addClassNames("productsto-segments-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("productExtId").setAutoWidth(true);
        grid.addColumn("segmentExtId").setAutoWidth(true);
        grid.addColumn("productName").setAutoWidth(true);
        grid.addColumn("segment").setAutoWidth(true);
        LitRenderer<PROD_SEG> activeRenderer = LitRenderer.<PROD_SEG>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.setItems(query -> pROD_SEGService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROD_SEG_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProductstoSegmentsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PROD_SEG.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(productExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("productExtId");
        binder.forField(segmentExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("segmentExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.pROD_SEG == null) {
                    this.pROD_SEG = new PROD_SEG();
                }
                binder.writeBean(this.pROD_SEG);
                pROD_SEGService.update(this.pROD_SEG);
                clearForm();
                refreshGrid();
                Notification.show("PROD_SEG details stored.");
                UI.getCurrent().navigate(ProductstoSegmentsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the pROD_SEG details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> pROD_SEGId = event.getRouteParameters().get(PROD_SEG_ID).map(UUID::fromString);
        if (pROD_SEGId.isPresent()) {
            Optional<PROD_SEG> pROD_SEGFromBackend = pROD_SEGService.get(pROD_SEGId.get());
            if (pROD_SEGFromBackend.isPresent()) {
                populateForm(pROD_SEGFromBackend.get());
            } else {
                Notification.show(String.format("The requested pROD_SEG was not found, ID = %s", pROD_SEGId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProductstoSegmentsView.class);
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
        productExtId = new TextField("Product Ext Id");
        segmentExtId = new TextField("Segment Ext Id");
        productName = new TextField("Product Name");
        segment = new TextField("Segment");
        active = new Checkbox("Active");
        formLayout.add(productExtId, segmentExtId, productName, segment, active);

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

    private void populateForm(PROD_SEG value) {
        this.pROD_SEG = value;
        binder.readBean(this.pROD_SEG);

    }
}
