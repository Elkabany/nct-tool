package com.cae.nct.views.needscategory;

import com.cae.nct.data.entity.NEEDCAT;
import com.cae.nct.data.service.NEEDCATService;
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

@PageTitle("Needs Category")
@Route(value = "NeedsCategory/:nEEDCATID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class NeedsCategoryView extends Div implements BeforeEnterObserver {

    private final String NEEDCAT_ID = "nEEDCATID";
    private final String NEEDCAT_EDIT_ROUTE_TEMPLATE = "NeedsCategory/%s/edit";

    private final Grid<NEEDCAT> grid = new Grid<>(NEEDCAT.class, false);

    private TextField nameEn;
    private TextField nameAr;
    private TextField icon;
    private TextField picture;
    private TextField descEn;
    private TextField descAr;
    private TextField tipsLink;
    private TextField tipsDescEn;
    private TextField tipsDescAr;
    private TextField simulatorLink;
    private Checkbox active;
    private TextField extId;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<NEEDCAT> binder;

    private NEEDCAT nEEDCAT;

    private final NEEDCATService nEEDCATService;

    @Autowired
    public NeedsCategoryView(NEEDCATService nEEDCATService) {
        this.nEEDCATService = nEEDCATService;
        addClassNames("needs-category-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nameEn").setAutoWidth(true);
        grid.addColumn("nameAr").setAutoWidth(true);
        grid.addColumn("icon").setAutoWidth(true);
        grid.addColumn("picture").setAutoWidth(true);
        grid.addColumn("descEn").setAutoWidth(true);
        grid.addColumn("descAr").setAutoWidth(true);
        grid.addColumn("tipsLink").setAutoWidth(true);
        grid.addColumn("tipsDescEn").setAutoWidth(true);
        grid.addColumn("tipsDescAr").setAutoWidth(true);
        grid.addColumn("simulatorLink").setAutoWidth(true);
        LitRenderer<NEEDCAT> activeRenderer = LitRenderer.<NEEDCAT>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", active -> active.isActive() ? "check" : "minus").withProperty("color",
                        active -> active.isActive()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(activeRenderer).setHeader("Active").setAutoWidth(true);

        grid.addColumn("extId").setAutoWidth(true);
        grid.setItems(query -> nEEDCATService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(NEEDCAT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(NeedsCategoryView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(NEEDCAT.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.nEEDCAT == null) {
                    this.nEEDCAT = new NEEDCAT();
                }
                binder.writeBean(this.nEEDCAT);
                nEEDCATService.update(this.nEEDCAT);
                clearForm();
                refreshGrid();
                Notification.show("NEEDCAT details stored.");
                UI.getCurrent().navigate(NeedsCategoryView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the nEEDCAT details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> nEEDCATId = event.getRouteParameters().get(NEEDCAT_ID).map(UUID::fromString);
        if (nEEDCATId.isPresent()) {
            Optional<NEEDCAT> nEEDCATFromBackend = nEEDCATService.get(nEEDCATId.get());
            if (nEEDCATFromBackend.isPresent()) {
                populateForm(nEEDCATFromBackend.get());
            } else {
                Notification.show(String.format("The requested nEEDCAT was not found, ID = %s", nEEDCATId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(NeedsCategoryView.class);
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
        nameEn = new TextField("Name En");
        nameAr = new TextField("Name Ar");
        icon = new TextField("Icon");
        picture = new TextField("Picture");
        descEn = new TextField("Desc En");
        descAr = new TextField("Desc Ar");
        tipsLink = new TextField("Tips Link");
        tipsDescEn = new TextField("Tips Desc En");
        tipsDescAr = new TextField("Tips Desc Ar");
        simulatorLink = new TextField("Simulator Link");
        active = new Checkbox("Active");
        extId = new TextField("Ext Id");
        formLayout.add(nameEn, nameAr, icon, picture, descEn, descAr, tipsLink, tipsDescEn, tipsDescAr, simulatorLink,
                active, extId);

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

    private void populateForm(NEEDCAT value) {
        this.nEEDCAT = value;
        binder.readBean(this.nEEDCAT);

    }
}
