package com.cae.nct.views.portfoliosegment;

import com.cae.nct.data.entity.PortfolioSegment;
import com.cae.nct.data.service.PortfolioSegmentService;
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

@PageTitle("Portfolio Segment")
@Route(value = "PortfolioSegment/:portfolioSegmentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class PortfolioSegmentView extends Div implements BeforeEnterObserver {

    private final String PORTFOLIOSEGMENT_ID = "portfolioSegmentID";
    private final String PORTFOLIOSEGMENT_EDIT_ROUTE_TEMPLATE = "PortfolioSegment/%s/edit";

    private final Grid<PortfolioSegment> grid = new Grid<>(PortfolioSegment.class, false);

    private TextField permittedVolume;
    private Checkbox active;
    private TextField type;
    private TextField segment;
    private TextField typeExtId;
    private TextField segmentExtId;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PortfolioSegment> binder;

    private PortfolioSegment portfolioSegment;

    private final PortfolioSegmentService portfolioSegmentService;

    @Autowired
    public PortfolioSegmentView(PortfolioSegmentService portfolioSegmentService) {
        this.portfolioSegmentService = portfolioSegmentService;
        addClassNames("portfolio-segment-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("permittedVolume").setAutoWidth(true);
        LitRenderer<PortfolioSegment> activeRenderer = LitRenderer.<PortfolioSegment>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.addColumn("type").setAutoWidth(true);
        grid.addColumn("segment").setAutoWidth(true);
        grid.addColumn("typeExtId").setAutoWidth(true);
        grid.addColumn("segmentExtId").setAutoWidth(true);
        grid.setItems(query -> portfolioSegmentService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PORTFOLIOSEGMENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PortfolioSegmentView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PortfolioSegment.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(permittedVolume).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("permittedVolume");
        binder.forField(typeExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("typeExtId");
        binder.forField(segmentExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("segmentExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.portfolioSegment == null) {
                    this.portfolioSegment = new PortfolioSegment();
                }
                binder.writeBean(this.portfolioSegment);
                portfolioSegmentService.update(this.portfolioSegment);
                clearForm();
                refreshGrid();
                Notification.show("PortfolioSegment details stored.");
                UI.getCurrent().navigate(PortfolioSegmentView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the portfolioSegment details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> portfolioSegmentId = event.getRouteParameters().get(PORTFOLIOSEGMENT_ID).map(UUID::fromString);
        if (portfolioSegmentId.isPresent()) {
            Optional<PortfolioSegment> portfolioSegmentFromBackend = portfolioSegmentService
                    .get(portfolioSegmentId.get());
            if (portfolioSegmentFromBackend.isPresent()) {
                populateForm(portfolioSegmentFromBackend.get());
            } else {
                Notification.show(String.format("The requested portfolioSegment was not found, ID = %s",
                        portfolioSegmentId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PortfolioSegmentView.class);
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
        permittedVolume = new TextField("Permitted Volume");
        active = new Checkbox("Active");
        type = new TextField("Type");
        segment = new TextField("Segment");
        typeExtId = new TextField("Type Ext Id");
        segmentExtId = new TextField("Segment Ext Id");
        formLayout.add(permittedVolume, active, type, segment, typeExtId, segmentExtId);

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

    private void populateForm(PortfolioSegment value) {
        this.portfolioSegment = value;
        binder.readBean(this.portfolioSegment);

    }
}
