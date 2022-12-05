package com.cae.nct.views.needscategorytoprojectcategory;

import com.cae.nct.data.entity.NEEDCAT_PROJECTCAT;
import com.cae.nct.data.service.NEEDCAT_PROJECTCATService;
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

@PageTitle("Needs Category to Project Category")
@Route(value = "NeedCatProject/:nEEDCAT_PROJECTCATID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class NeedsCategorytoProjectCategoryView extends Div implements BeforeEnterObserver {

    private final String NEEDCAT_PROJECTCAT_ID = "nEEDCAT_PROJECTCATID";
    private final String NEEDCAT_PROJECTCAT_EDIT_ROUTE_TEMPLATE = "NeedCatProject/%s/edit";

    private final Grid<NEEDCAT_PROJECTCAT> grid = new Grid<>(NEEDCAT_PROJECTCAT.class, false);

    private TextField needCategory;
    private TextField projectCategory;
    private TextField needExtId;
    private TextField projectExtId;
    private Checkbox active;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<NEEDCAT_PROJECTCAT> binder;

    private NEEDCAT_PROJECTCAT nEEDCAT_PROJECTCAT;

    private final NEEDCAT_PROJECTCATService nEEDCAT_PROJECTCATService;

    @Autowired
    public NeedsCategorytoProjectCategoryView(NEEDCAT_PROJECTCATService nEEDCAT_PROJECTCATService) {
        this.nEEDCAT_PROJECTCATService = nEEDCAT_PROJECTCATService;
        addClassNames("needs-categoryto-project-category-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("needCategory").setAutoWidth(true);
        grid.addColumn("projectCategory").setAutoWidth(true);
        grid.addColumn("needExtId").setAutoWidth(true);
        grid.addColumn("projectExtId").setAutoWidth(true);
        LitRenderer<NEEDCAT_PROJECTCAT> activeRenderer = LitRenderer.<NEEDCAT_PROJECTCAT>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.setItems(query -> nEEDCAT_PROJECTCATService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent()
                        .navigate(String.format(NEEDCAT_PROJECTCAT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(NeedsCategorytoProjectCategoryView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(NEEDCAT_PROJECTCAT.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(needExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("needExtId");
        binder.forField(projectExtId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("projectExtId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.nEEDCAT_PROJECTCAT == null) {
                    this.nEEDCAT_PROJECTCAT = new NEEDCAT_PROJECTCAT();
                }
                binder.writeBean(this.nEEDCAT_PROJECTCAT);
                nEEDCAT_PROJECTCATService.update(this.nEEDCAT_PROJECTCAT);
                clearForm();
                refreshGrid();
                Notification.show("NEEDCAT_PROJECTCAT details stored.");
                UI.getCurrent().navigate(NeedsCategorytoProjectCategoryView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the nEEDCAT_PROJECTCAT details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> nEEDCAT_PROJECTCATId = event.getRouteParameters().get(NEEDCAT_PROJECTCAT_ID)
                .map(UUID::fromString);
        if (nEEDCAT_PROJECTCATId.isPresent()) {
            Optional<NEEDCAT_PROJECTCAT> nEEDCAT_PROJECTCATFromBackend = nEEDCAT_PROJECTCATService
                    .get(nEEDCAT_PROJECTCATId.get());
            if (nEEDCAT_PROJECTCATFromBackend.isPresent()) {
                populateForm(nEEDCAT_PROJECTCATFromBackend.get());
            } else {
                Notification.show(String.format("The requested nEEDCAT_PROJECTCAT was not found, ID = %s",
                        nEEDCAT_PROJECTCATId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(NeedsCategorytoProjectCategoryView.class);
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
        needCategory = new TextField("Need Category");
        projectCategory = new TextField("Project Category");
        needExtId = new TextField("Need Ext Id");
        projectExtId = new TextField("Project Ext Id");
        active = new Checkbox("Active");
        formLayout.add(needCategory, projectCategory, needExtId, projectExtId, active);

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

    private void populateForm(NEEDCAT_PROJECTCAT value) {
        this.nEEDCAT_PROJECTCAT = value;
        binder.readBean(this.nEEDCAT_PROJECTCAT);

    }
}
