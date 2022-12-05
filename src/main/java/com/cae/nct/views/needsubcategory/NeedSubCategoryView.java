package com.cae.nct.views.needsubcategory;

import com.cae.nct.data.entity.NEEDSUBCAT;
import com.cae.nct.data.service.NEEDSUBCATService;
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

@PageTitle("Need SubCategory")
@Route(value = "NeedSubCategory/:nEEDSUBCATID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class NeedSubCategoryView extends Div implements BeforeEnterObserver {

    private final String NEEDSUBCAT_ID = "nEEDSUBCATID";
    private final String NEEDSUBCAT_EDIT_ROUTE_TEMPLATE = "NeedSubCategory/%s/edit";

    private final Grid<NEEDSUBCAT> grid = new Grid<>(NEEDSUBCAT.class, false);

    private TextField category;
    private TextField nameEn;
    private TextField nameAr;
    private Checkbox active;
    private TextField extId;
    private TextField categoryExtId;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<NEEDSUBCAT> binder;

    private NEEDSUBCAT nEEDSUBCAT;

    private final NEEDSUBCATService nEEDSUBCATService;

    @Autowired
    public NeedSubCategoryView(NEEDSUBCATService nEEDSUBCATService) {
        this.nEEDSUBCATService = nEEDSUBCATService;
        addClassNames("need-sub-category-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("category").setAutoWidth(true);
        grid.addColumn("nameEn").setAutoWidth(true);
        grid.addColumn("nameAr").setAutoWidth(true);
        LitRenderer<NEEDSUBCAT> activeRenderer = LitRenderer.<NEEDSUBCAT>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.addColumn("extId").setAutoWidth(true);
        grid.addColumn("categoryExtId").setAutoWidth(true);
        grid.setItems(query -> nEEDSUBCATService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(NEEDSUBCAT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(NeedSubCategoryView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(NEEDSUBCAT.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");
        binder.forField(categoryExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("categoryExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.nEEDSUBCAT == null) {
                    this.nEEDSUBCAT = new NEEDSUBCAT();
                }
                binder.writeBean(this.nEEDSUBCAT);
                nEEDSUBCATService.update(this.nEEDSUBCAT);
                clearForm();
                refreshGrid();
                Notification.show("NEEDSUBCAT details stored.");
                UI.getCurrent().navigate(NeedSubCategoryView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the nEEDSUBCAT details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> nEEDSUBCATId = event.getRouteParameters().get(NEEDSUBCAT_ID).map(UUID::fromString);
        if (nEEDSUBCATId.isPresent()) {
            Optional<NEEDSUBCAT> nEEDSUBCATFromBackend = nEEDSUBCATService.get(nEEDSUBCATId.get());
            if (nEEDSUBCATFromBackend.isPresent()) {
                populateForm(nEEDSUBCATFromBackend.get());
            } else {
                Notification.show(String.format("The requested nEEDSUBCAT was not found, ID = %s", nEEDSUBCATId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(NeedSubCategoryView.class);
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
        nameEn = new TextField("Name En");
        nameAr = new TextField("Name Ar");
        active = new Checkbox("Active");
        extId = new TextField("Ext Id");
        categoryExtId = new TextField("Category Ext Id");
        formLayout.add(category, nameEn, nameAr, active, extId, categoryExtId);

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

    private void populateForm(NEEDSUBCAT value) {
        this.nEEDSUBCAT = value;
        binder.readBean(this.nEEDSUBCAT);

    }
}
