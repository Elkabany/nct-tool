package com.cae.nct.views.projectcategory;

import com.cae.nct.data.entity.PROJECTCAT;
import com.cae.nct.data.service.PROJECTCATService;
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

@PageTitle("Project Category")
@Route(value = "ProjectCat/:pROJECTCATID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class ProjectCategoryView extends Div implements BeforeEnterObserver {

    private final String PROJECTCAT_ID = "pROJECTCATID";
    private final String PROJECTCAT_EDIT_ROUTE_TEMPLATE = "ProjectCat/%s/edit";

    private final Grid<PROJECTCAT> grid = new Grid<>(PROJECTCAT.class, false);

    private TextField nameAr;
    private TextField nameEn;
    private Checkbox active;
    private TextField extId;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PROJECTCAT> binder;

    private PROJECTCAT pROJECTCAT;

    private final PROJECTCATService pROJECTCATService;

    @Autowired
    public ProjectCategoryView(PROJECTCATService pROJECTCATService) {
        this.pROJECTCATService = pROJECTCATService;
        addClassNames("project-category-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nameAr").setAutoWidth(true);
        grid.addColumn("nameEn").setAutoWidth(true);
        LitRenderer<PROJECTCAT> activeRenderer = LitRenderer.<PROJECTCAT>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.addColumn("extId").setAutoWidth(true);
        grid.setItems(query -> pROJECTCATService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROJECTCAT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProjectCategoryView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PROJECTCAT.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.pROJECTCAT == null) {
                    this.pROJECTCAT = new PROJECTCAT();
                }
                binder.writeBean(this.pROJECTCAT);
                pROJECTCATService.update(this.pROJECTCAT);
                clearForm();
                refreshGrid();
                Notification.show("PROJECTCAT details stored.");
                UI.getCurrent().navigate(ProjectCategoryView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the pROJECTCAT details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> pROJECTCATId = event.getRouteParameters().get(PROJECTCAT_ID).map(UUID::fromString);
        if (pROJECTCATId.isPresent()) {
            Optional<PROJECTCAT> pROJECTCATFromBackend = pROJECTCATService.get(pROJECTCATId.get());
            if (pROJECTCATFromBackend.isPresent()) {
                populateForm(pROJECTCATFromBackend.get());
            } else {
                Notification.show(String.format("The requested pROJECTCAT was not found, ID = %s", pROJECTCATId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProjectCategoryView.class);
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
        nameAr = new TextField("Name Ar");
        nameEn = new TextField("Name En");
        active = new Checkbox("Active");
        extId = new TextField("Ext Id");
        formLayout.add(nameAr, nameEn, active, extId);

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

    private void populateForm(PROJECTCAT value) {
        this.pROJECTCAT = value;
        binder.readBean(this.pROJECTCAT);

    }
}
