package com.cae.nct.views.productsubgroup;

import com.cae.nct.data.entity.PRODSUBGR;
import com.cae.nct.data.service.PRODSUBGRService;
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

@PageTitle("Product SubGroup")
@Route(value = "ProductSubGroup/:pRODSUBGRID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class ProductSubGroupView extends Div implements BeforeEnterObserver {

    private final String PRODSUBGR_ID = "pRODSUBGRID";
    private final String PRODSUBGR_EDIT_ROUTE_TEMPLATE = "ProductSubGroup/%s/edit";

    private final Grid<PRODSUBGR> grid = new Grid<>(PRODSUBGR.class, false);

    private TextField group;
    private TextField code;
    private TextField nameEn;
    private TextField nameAr;
    private TextField order;
    private Checkbox active;
    private TextField extId;
    private TextField role;
    private TextField groupExtId;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PRODSUBGR> binder;

    private PRODSUBGR pRODSUBGR;

    private final PRODSUBGRService pRODSUBGRService;

    @Autowired
    public ProductSubGroupView(PRODSUBGRService pRODSUBGRService) {
        this.pRODSUBGRService = pRODSUBGRService;
        addClassNames("product-sub-group-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("group").setAutoWidth(true);
        grid.addColumn("code").setAutoWidth(true);
        grid.addColumn("nameEn").setAutoWidth(true);
        grid.addColumn("nameAr").setAutoWidth(true);
        grid.addColumn("order").setAutoWidth(true);
        LitRenderer<PRODSUBGR> activeRenderer = LitRenderer.<PRODSUBGR>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.addColumn("extId").setAutoWidth(true);
        grid.addColumn("role").setAutoWidth(true);
        grid.addColumn("groupExtId").setAutoWidth(true);
        grid.setItems(query -> pRODSUBGRService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PRODSUBGR_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProductSubGroupView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PRODSUBGR.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(order).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("order");
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");
        binder.forField(groupExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("groupExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.pRODSUBGR == null) {
                    this.pRODSUBGR = new PRODSUBGR();
                }
                binder.writeBean(this.pRODSUBGR);
                pRODSUBGRService.update(this.pRODSUBGR);
                clearForm();
                refreshGrid();
                Notification.show("PRODSUBGR details stored.");
                UI.getCurrent().navigate(ProductSubGroupView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the pRODSUBGR details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> pRODSUBGRId = event.getRouteParameters().get(PRODSUBGR_ID).map(UUID::fromString);
        if (pRODSUBGRId.isPresent()) {
            Optional<PRODSUBGR> pRODSUBGRFromBackend = pRODSUBGRService.get(pRODSUBGRId.get());
            if (pRODSUBGRFromBackend.isPresent()) {
                populateForm(pRODSUBGRFromBackend.get());
            } else {
                Notification.show(String.format("The requested pRODSUBGR was not found, ID = %s", pRODSUBGRId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProductSubGroupView.class);
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
        group = new TextField("Group");
        code = new TextField("Code");
        nameEn = new TextField("Name En");
        nameAr = new TextField("Name Ar");
        order = new TextField("Order");
        active = new Checkbox("Active");
        extId = new TextField("Ext Id");
        role = new TextField("Role");
        groupExtId = new TextField("Group Ext Id");
        formLayout.add(group, code, nameEn, nameAr, order, active, extId, role, groupExtId);

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

    private void populateForm(PRODSUBGR value) {
        this.pRODSUBGR = value;
        binder.readBean(this.pRODSUBGR);

    }
}
