package com.cae.nct.views.targetmeeting;

import com.cae.nct.data.entity.TargetMeeting;
import com.cae.nct.data.service.TargetMeetingService;
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

@PageTitle("Target Meeting")
@Route(value = "TargetMeeting/:targetMeetingID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
public class TargetMeetingView extends Div implements BeforeEnterObserver {

    private final String TARGETMEETING_ID = "targetMeetingID";
    private final String TARGETMEETING_EDIT_ROUTE_TEMPLATE = "TargetMeeting/%s/edit";

    private final Grid<TargetMeeting> grid = new Grid<>(TargetMeeting.class, false);

    private TextField positionId;
    private TextField name;
    private TextField number;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<TargetMeeting> binder;

    private TargetMeeting targetMeeting;

    private final TargetMeetingService targetMeetingService;

    @Autowired
    public TargetMeetingView(TargetMeetingService targetMeetingService) {
        this.targetMeetingService = targetMeetingService;
        addClassNames("target-meeting-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("positionId").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("number").setAutoWidth(true);
        grid.setItems(query -> targetMeetingService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TARGETMEETING_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TargetMeetingView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(TargetMeeting.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.targetMeeting == null) {
                    this.targetMeeting = new TargetMeeting();
                }
                binder.writeBean(this.targetMeeting);
                targetMeetingService.update(this.targetMeeting);
                clearForm();
                refreshGrid();
                Notification.show("TargetMeeting details stored.");
                UI.getCurrent().navigate(TargetMeetingView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the targetMeeting details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> targetMeetingId = event.getRouteParameters().get(TARGETMEETING_ID).map(UUID::fromString);
        if (targetMeetingId.isPresent()) {
            Optional<TargetMeeting> targetMeetingFromBackend = targetMeetingService.get(targetMeetingId.get());
            if (targetMeetingFromBackend.isPresent()) {
                populateForm(targetMeetingFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested targetMeeting was not found, ID = %s", targetMeetingId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TargetMeetingView.class);
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
        positionId = new TextField("Position Id");
        name = new TextField("Name");
        number = new TextField("Number");
        formLayout.add(positionId, name, number);

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

    private void populateForm(TargetMeeting value) {
        this.targetMeeting = value;
        binder.readBean(this.targetMeeting);

    }
}
