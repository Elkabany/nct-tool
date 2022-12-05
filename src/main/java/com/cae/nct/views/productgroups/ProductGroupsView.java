package com.cae.nct.views.productgroups;

import com.cae.nct.data.entity.PRODGR;
import com.cae.nct.data.service.PRODGRService;
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

@PageTitle("Product Groups")
@Route(value = "ProductGroups/:pRODGRID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class ProductGroupsView extends Div implements BeforeEnterObserver {

    private final String PRODGR_ID = "pRODGRID";
    private final String PRODGR_EDIT_ROUTE_TEMPLATE = "ProductGroups/%s/edit";

    private final Grid<PRODGR> grid = new Grid<>(PRODGR.class, false);

    private TextField code;
    private TextField nameEn;
    private TextField nameAr;
    private TextField picture;
    private TextField descEn;
    private TextField descAr;
    private Checkbox active;
    private TextField extId;
    private TextField role;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PRODGR> binder;

    private PRODGR pRODGR;

    private final PRODGRService pRODGRService;

    @Autowired
    public ProductGroupsView(PRODGRService pRODGRService) {
        this.pRODGRService = pRODGRService;
        addClassNames("product-groups-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("code").setAutoWidth(true);
        grid.addColumn("nameEn").setAutoWidth(true);
        grid.addColumn("nameAr").setAutoWidth(true);
        grid.addColumn("picture").setAutoWidth(true);
        grid.addColumn("descEn").setAutoWidth(true);
        grid.addColumn("descAr").setAutoWidth(true);
        LitRenderer<PRODGR> activeRenderer = LitRenderer.<PRODGR>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.addColumn("extId").setAutoWidth(true);
        grid.addColumn("role").setAutoWidth(true);
        grid.setItems(query -> pRODGRService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PRODGR_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProductGroupsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PRODGR.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.pRODGR == null) {
                    this.pRODGR = new PRODGR();
                }
                binder.writeBean(this.pRODGR);
                pRODGRService.update(this.pRODGR);
                clearForm();
                refreshGrid();
                Notification.show("PRODGR details stored.");
                UI.getCurrent().navigate(ProductGroupsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the pRODGR details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> pRODGRId = event.getRouteParameters().get(PRODGR_ID).map(UUID::fromString);
        if (pRODGRId.isPresent()) {
            Optional<PRODGR> pRODGRFromBackend = pRODGRService.get(pRODGRId.get());
            if (pRODGRFromBackend.isPresent()) {
                populateForm(pRODGRFromBackend.get());
            } else {
                Notification.show(String.format("The requested pRODGR was not found, ID = %s", pRODGRId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProductGroupsView.class);
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
        code = new TextField("Code");
        nameEn = new TextField("Name En");
        nameAr = new TextField("Name Ar");
        picture = new TextField("Picture");
        descEn = new TextField("Desc En");
        descAr = new TextField("Desc Ar");
        active = new Checkbox("Active");
        extId = new TextField("Ext Id");
        role = new TextField("Role");
        formLayout.add(code, nameEn, nameAr, picture, descEn, descAr, active, extId, role);

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

    private void populateForm(PRODGR value) {
        this.pRODGR = value;
        binder.readBean(this.pRODGR);

    }
}
