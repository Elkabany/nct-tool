package com.cae.nct.views.needsubcategorytoproducts;

import com.cae.nct.data.entity.NEEDSUBCAT_PROD;
import com.cae.nct.data.service.NEEDSUBCAT_PRODService;
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

@PageTitle("Need SubCategory to Products")
@Route(value = "NeedSubCatProduct/:nEEDSUBCAT_PRODID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class NeedSubCategorytoProductsView extends Div implements BeforeEnterObserver {

    private final String NEEDSUBCAT_PROD_ID = "nEEDSUBCAT_PRODID";
    private final String NEEDSUBCAT_PROD_EDIT_ROUTE_TEMPLATE = "NeedSubCatProduct/%s/edit";

    private final Grid<NEEDSUBCAT_PROD> grid = new Grid<>(NEEDSUBCAT_PROD.class, false);

    private TextField needSubcategory;
    private TextField needSubExtId;
    private TextField product;
    private TextField productExtId;
    private Checkbox active;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<NEEDSUBCAT_PROD> binder;

    private NEEDSUBCAT_PROD nEEDSUBCAT_PROD;

    private final NEEDSUBCAT_PRODService nEEDSUBCAT_PRODService;

    @Autowired
    public NeedSubCategorytoProductsView(NEEDSUBCAT_PRODService nEEDSUBCAT_PRODService) {
        this.nEEDSUBCAT_PRODService = nEEDSUBCAT_PRODService;
        addClassNames("need-sub-categoryto-products-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("needSubcategory").setAutoWidth(true);
        grid.addColumn("needSubExtId").setAutoWidth(true);
        grid.addColumn("product").setAutoWidth(true);
        grid.addColumn("productExtId").setAutoWidth(true);
        LitRenderer<NEEDSUBCAT_PROD> activeRenderer = LitRenderer.<NEEDSUBCAT_PROD>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.setItems(query -> nEEDSUBCAT_PRODService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(NEEDSUBCAT_PROD_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(NeedSubCategorytoProductsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(NEEDSUBCAT_PROD.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(needSubExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("needSubExtId");
        binder.forField(productExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("productExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.nEEDSUBCAT_PROD == null) {
                    this.nEEDSUBCAT_PROD = new NEEDSUBCAT_PROD();
                }
                binder.writeBean(this.nEEDSUBCAT_PROD);
                nEEDSUBCAT_PRODService.update(this.nEEDSUBCAT_PROD);
                clearForm();
                refreshGrid();
                Notification.show("NEEDSUBCAT_PROD details stored.");
                UI.getCurrent().navigate(NeedSubCategorytoProductsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the nEEDSUBCAT_PROD details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> nEEDSUBCAT_PRODId = event.getRouteParameters().get(NEEDSUBCAT_PROD_ID).map(UUID::fromString);
        if (nEEDSUBCAT_PRODId.isPresent()) {
            Optional<NEEDSUBCAT_PROD> nEEDSUBCAT_PRODFromBackend = nEEDSUBCAT_PRODService.get(nEEDSUBCAT_PRODId.get());
            if (nEEDSUBCAT_PRODFromBackend.isPresent()) {
                populateForm(nEEDSUBCAT_PRODFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested nEEDSUBCAT_PROD was not found, ID = %s", nEEDSUBCAT_PRODId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(NeedSubCategorytoProductsView.class);
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
        needSubcategory = new TextField("Need Subcategory");
        needSubExtId = new TextField("Need Sub Ext Id");
        product = new TextField("Product");
        productExtId = new TextField("Product Ext Id");
        active = new Checkbox("Active");
        formLayout.add(needSubcategory, needSubExtId, product, productExtId, active);

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

    private void populateForm(NEEDSUBCAT_PROD value) {
        this.nEEDSUBCAT_PROD = value;
        binder.readBean(this.nEEDSUBCAT_PROD);

    }
}
