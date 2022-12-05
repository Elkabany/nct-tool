package com.cae.nct.views.portfoliotype;

import com.cae.nct.data.entity.PortfolioType;
import com.cae.nct.data.service.PortfolioTypeService;
import com.cae.nct.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
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

@PageTitle("Portfolio Type")
@Route(value = "PortfolioType/:portfolioTypeID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
public class PortfolioTypeView extends Div implements BeforeEnterObserver {

    private final String PORTFOLIOTYPE_ID = "portfolioTypeID";
    private final String PORTFOLIOTYPE_EDIT_ROUTE_TEMPLATE = "PortfolioType/%s/edit";

    private final Grid<PortfolioType> grid = new Grid<>(PortfolioType.class, false);

    private TextField name;
    private TextField code;
    private TextField extId;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PortfolioType> binder;

    private PortfolioType portfolioType;

    private final PortfolioTypeService portfolioTypeService;

    @Autowired
    public PortfolioTypeView(PortfolioTypeService portfolioTypeService) {
        this.portfolioTypeService = portfolioTypeService;
        addClassNames("portfolio-type-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("code").setAutoWidth(true);
        grid.addColumn("extId").setAutoWidth(true);
        grid.setItems(query -> portfolioTypeService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PORTFOLIOTYPE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PortfolioTypeView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PortfolioType.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(extId).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("extId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.portfolioType == null) {
                    this.portfolioType = new PortfolioType();
                }
                binder.writeBean(this.portfolioType);
                portfolioTypeService.update(this.portfolioType);
                clearForm();
                refreshGrid();
                Notification.show("PortfolioType details stored.");
                UI.getCurrent().navigate(PortfolioTypeView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the portfolioType details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> portfolioTypeId = event.getRouteParameters().get(PORTFOLIOTYPE_ID).map(UUID::fromString);
        if (portfolioTypeId.isPresent()) {
            Optional<PortfolioType> portfolioTypeFromBackend = portfolioTypeService.get(portfolioTypeId.get());
            if (portfolioTypeFromBackend.isPresent()) {
                populateForm(portfolioTypeFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested portfolioType was not found, ID = %s", portfolioTypeId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PortfolioTypeView.class);
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
        name = new TextField("Name");
        code = new TextField("Code");
        extId = new TextField("Ext Id");
        formLayout.add(name, code, extId);

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

    private void populateForm(PortfolioType value) {
        this.portfolioType = value;
        binder.readBean(this.portfolioType);

    }
}
