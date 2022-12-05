package com.cae.nct.views.segments;

import com.cae.nct.data.entity.Segment;
import com.cae.nct.data.service.SegmentService;
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

@PageTitle("Segments")
@Route(value = "Segments/:segmentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class SegmentsView extends Div implements BeforeEnterObserver {

    private final String SEGMENT_ID = "segmentID";
    private final String SEGMENT_EDIT_ROUTE_TEMPLATE = "Segments/%s/edit";

    private final Grid<Segment> grid = new Grid<>(Segment.class, false);

    private TextField name;
    private Checkbox prospect;
    private TextField extId;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Segment> binder;

    private Segment segment;

    private final SegmentService segmentService;

    @Autowired
    public SegmentsView(SegmentService segmentService) {
        this.segmentService = segmentService;
        addClassNames("segments-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        LitRenderer<Segment> prospectRenderer = LitRenderer.<Segment>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", prospect -> prospect.isProspect() ? "check" : "minus").withProperty("color",
                        prospect -> prospect.isProspect()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(prospectRenderer).setHeader("Prospect").setAutoWidth(true);

        grid.addColumn("extId").setAutoWidth(true);
        grid.setItems(query -> segmentService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SEGMENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(SegmentsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Segment.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.segment == null) {
                    this.segment = new Segment();
                }
                binder.writeBean(this.segment);
                segmentService.update(this.segment);
                clearForm();
                refreshGrid();
                Notification.show("Segment details stored.");
                UI.getCurrent().navigate(SegmentsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the segment details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> segmentId = event.getRouteParameters().get(SEGMENT_ID).map(UUID::fromString);
        if (segmentId.isPresent()) {
            Optional<Segment> segmentFromBackend = segmentService.get(segmentId.get());
            if (segmentFromBackend.isPresent()) {
                populateForm(segmentFromBackend.get());
            } else {
                Notification.show(String.format("The requested segment was not found, ID = %s", segmentId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(SegmentsView.class);
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
        name = new TextField("Name");
        prospect = new Checkbox("Prospect");
        extId = new TextField("Ext Id");
        formLayout.add(name, prospect, extId);

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

    private void populateForm(Segment value) {
        this.segment = value;
        binder.readBean(this.segment);

    }
}
