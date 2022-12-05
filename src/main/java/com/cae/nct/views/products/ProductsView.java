package com.cae.nct.views.products;

import com.cae.nct.data.entity.PROD;
import com.cae.nct.data.service.PRODService;
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

@PageTitle("Products")
@Route(value = "Products/:pRODID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
@Uses(Icon.class)
@Uses(Icon.class)
@Uses(Icon.class)
public class ProductsView extends Div implements BeforeEnterObserver {

    private final String PROD_ID = "pRODID";
    private final String PROD_EDIT_ROUTE_TEMPLATE = "Products/%s/edit";

    private final Grid<PROD> grid = new Grid<>(PROD.class, false);

    private TextField code;
    private TextField nameEn;
    private TextField nameAr;
    private TextField picture;
    private TextField shortDescEn;
    private TextField shortDescAr;
    private TextField longLink;
    private TextField longDescEn;
    private TextField longDescAr;
    private TextField tipsLink;
    private TextField tipsDescEn;
    private TextField tipsDescAr;
    private TextField simulatorLink;
    private TextField color;
    private Checkbox hasAmount;
    private Checkbox hasPieces;
    private Checkbox active;
    private Checkbox restricted;
    private TextField extId;
    private TextField role;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PROD> binder;

    private PROD pROD;

    private final PRODService pRODService;

    @Autowired
    public ProductsView(PRODService pRODService) {
        this.pRODService = pRODService;
        addClassNames("products-view");

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
        grid.addColumn("shortDescEn").setAutoWidth(true);
        grid.addColumn("shortDescAr").setAutoWidth(true);
        grid.addColumn("longLink").setAutoWidth(true);
        grid.addColumn("longDescEn").setAutoWidth(true);
        grid.addColumn("longDescAr").setAutoWidth(true);
        grid.addColumn("tipsLink").setAutoWidth(true);
        grid.addColumn("tipsDescEn").setAutoWidth(true);
        grid.addColumn("tipsDescAr").setAutoWidth(true);
        grid.addColumn("simulatorLink").setAutoWidth(true);
        grid.addColumn("color").setAutoWidth(true);
        LitRenderer<PROD> hasAmountRenderer = LitRenderer.<PROD>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", hasAmount -> hasAmount.isHasAmount() ? "check" : "minus").withProperty("color",
                        hasAmount -> hasAmount.isHasAmount()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(hasAmountRenderer).setHeader("Has Amount").setAutoWidth(true);

        LitRenderer<PROD> hasPiecesRenderer = LitRenderer.<PROD>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", hasPieces -> hasPieces.isHasPieces() ? "check" : "minus").withProperty("color",
                        hasPieces -> hasPieces.isHasPieces()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(hasPiecesRenderer).setHeader("Has Pieces").setAutoWidth(true);

        LitRenderer<PROD> activeRenderer = LitRenderer.<PROD>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        LitRenderer<PROD> restrictedRenderer = LitRenderer.<PROD>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", restricted -> restricted.isRestricted() ? "check" : "minus").withProperty("color",
                        restricted -> restricted.isRestricted()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(restrictedRenderer).setHeader("Restricted").setAutoWidth(true);

        grid.addColumn("extId").setAutoWidth(true);
        grid.addColumn("role").setAutoWidth(true);
        grid.setItems(query -> pRODService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROD_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProductsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PROD.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.pROD == null) {
                    this.pROD = new PROD();
                }
                binder.writeBean(this.pROD);
                pRODService.update(this.pROD);
                clearForm();
                refreshGrid();
                Notification.show("PROD details stored.");
                UI.getCurrent().navigate(ProductsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the pROD details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> pRODId = event.getRouteParameters().get(PROD_ID).map(UUID::fromString);
        if (pRODId.isPresent()) {
            Optional<PROD> pRODFromBackend = pRODService.get(pRODId.get());
            if (pRODFromBackend.isPresent()) {
                populateForm(pRODFromBackend.get());
            } else {
                Notification.show(String.format("The requested pROD was not found, ID = %s", pRODId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProductsView.class);
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
        shortDescEn = new TextField("Short Desc En");
        shortDescAr = new TextField("Short Desc Ar");
        longLink = new TextField("Long Link");
        longDescEn = new TextField("Long Desc En");
        longDescAr = new TextField("Long Desc Ar");
        tipsLink = new TextField("Tips Link");
        tipsDescEn = new TextField("Tips Desc En");
        tipsDescAr = new TextField("Tips Desc Ar");
        simulatorLink = new TextField("Simulator Link");
        color = new TextField("Color");
        hasAmount = new Checkbox("Has Amount");
        hasPieces = new Checkbox("Has Pieces");
        active = new Checkbox("Active");
        restricted = new Checkbox("Restricted");
        extId = new TextField("Ext Id");
        role = new TextField("Role");
        formLayout.add(code, nameEn, nameAr, picture, shortDescEn, shortDescAr, longLink, longDescEn, longDescAr,
                tipsLink, tipsDescEn, tipsDescAr, simulatorLink, color, hasAmount, hasPieces, active, restricted, extId,
                role);

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

    private void populateForm(PROD value) {
        this.pROD = value;
        binder.readBean(this.pROD);

    }
}
