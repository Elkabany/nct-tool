package com.cae.nct.views.producttoproductsubgroup;

import com.cae.nct.data.entity.PROD_PRODSUBGR;
import com.cae.nct.data.service.PROD_PRODSUBGRService;
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

@PageTitle("Product to Product Subgroup")
@Route(value = "Product_ProdSubGroup/:pROD_PRODSUBGRID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class ProducttoProductSubgroupView extends Div implements BeforeEnterObserver {

    private final String PROD_PRODSUBGR_ID = "pROD_PRODSUBGRID";
    private final String PROD_PRODSUBGR_EDIT_ROUTE_TEMPLATE = "Product_ProdSubGroup/%s/edit";

    private final Grid<PROD_PRODSUBGR> grid = new Grid<>(PROD_PRODSUBGR.class, false);

    private TextField productExtId;
    private TextField subgroupExtId;
    private TextField productName;
    private TextField subgroupName;
    private Checkbox active;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PROD_PRODSUBGR> binder;

    private PROD_PRODSUBGR pROD_PRODSUBGR;

    private final PROD_PRODSUBGRService pROD_PRODSUBGRService;

    @Autowired
    public ProducttoProductSubgroupView(PROD_PRODSUBGRService pROD_PRODSUBGRService) {
        this.pROD_PRODSUBGRService = pROD_PRODSUBGRService;
        addClassNames("productto-product-subgroup-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("productExtId").setAutoWidth(true);
        grid.addColumn("subgroupExtId").setAutoWidth(true);
        grid.addColumn("productName").setAutoWidth(true);
        grid.addColumn("subgroupName").setAutoWidth(true);
        LitRenderer<PROD_PRODSUBGR> activeRenderer = LitRenderer.<PROD_PRODSUBGR>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.setItems(query -> pROD_PRODSUBGRService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROD_PRODSUBGR_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProducttoProductSubgroupView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PROD_PRODSUBGR.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(productExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("productExtId");
        binder.forField(subgroupExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("subgroupExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.pROD_PRODSUBGR == null) {
                    this.pROD_PRODSUBGR = new PROD_PRODSUBGR();
                }
                binder.writeBean(this.pROD_PRODSUBGR);
                pROD_PRODSUBGRService.update(this.pROD_PRODSUBGR);
                clearForm();
                refreshGrid();
                Notification.show("PROD_PRODSUBGR details stored.");
                UI.getCurrent().navigate(ProducttoProductSubgroupView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the pROD_PRODSUBGR details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> pROD_PRODSUBGRId = event.getRouteParameters().get(PROD_PRODSUBGR_ID).map(UUID::fromString);
        if (pROD_PRODSUBGRId.isPresent()) {
            Optional<PROD_PRODSUBGR> pROD_PRODSUBGRFromBackend = pROD_PRODSUBGRService.get(pROD_PRODSUBGRId.get());
            if (pROD_PRODSUBGRFromBackend.isPresent()) {
                populateForm(pROD_PRODSUBGRFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested pROD_PRODSUBGR was not found, ID = %s", pROD_PRODSUBGRId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProducttoProductSubgroupView.class);
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
        subgroupExtId = new TextField("Subgroup Ext Id");
        productName = new TextField("Product Name");
        subgroupName = new TextField("Subgroup Name");
        active = new Checkbox("Active");
        formLayout.add(productExtId, subgroupExtId, productName, subgroupName, active);

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

    private void populateForm(PROD_PRODSUBGR value) {
        this.pROD_PRODSUBGR = value;
        binder.readBean(this.pROD_PRODSUBGR);

    }
}
