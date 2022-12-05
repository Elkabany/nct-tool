package com.cae.nct.views;

import com.cae.nct.components.appnav.AppNav;
import com.cae.nct.components.appnav.AppNavItem;
import com.cae.nct.data.entity.User;
import com.cae.nct.security.AuthenticatedUser;
import com.cae.nct.views.about.AboutView;
import com.cae.nct.views.admin.AdminView;
import com.cae.nct.views.helloworld.HelloWorldView;
import com.cae.nct.views.needscategory.NeedsCategoryView;
import com.cae.nct.views.needscategorytoprojectcategory.NeedsCategorytoProjectCategoryView;
import com.cae.nct.views.needscategorytosegments.NeedsCategorytoSegmentsView;
import com.cae.nct.views.needsubcategory.NeedSubCategoryView;
import com.cae.nct.views.needsubcategorytoproducts.NeedSubCategorytoProductsView;
import com.cae.nct.views.opportunitycatalog.OpportunityCatalogView;
import com.cae.nct.views.opportunitysubcatalog.OpportunitySubCatalogView;
import com.cae.nct.views.portfoliosegment.PortfolioSegmentView;
import com.cae.nct.views.portfoliotype.PortfolioTypeView;
import com.cae.nct.views.productgroups.ProductGroupsView;
import com.cae.nct.views.products.ProductsView;
import com.cae.nct.views.productstosegments.ProductstoSegmentsView;
import com.cae.nct.views.productsubgroup.ProductSubGroupView;
import com.cae.nct.views.producttoproductsubgroup.ProducttoProductSubgroupView;
import com.cae.nct.views.projectcategory.ProjectCategoryView;
import com.cae.nct.views.segments.SegmentsView;
import com.cae.nct.views.targetmeeting.TargetMeetingView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("NCT Tool");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        if (accessChecker.hasAccess(HelloWorldView.class)) {
            nav.addItem(new AppNavItem("Hello World", HelloWorldView.class, "la la-globe"));

        }
        if (accessChecker.hasAccess(ProductGroupsView.class)) {
            nav.addItem(new AppNavItem("Product Groups", ProductGroupsView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(ProductSubGroupView.class)) {
            nav.addItem(new AppNavItem("Product SubGroup", ProductSubGroupView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(ProductsView.class)) {
            nav.addItem(new AppNavItem("Products", ProductsView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(ProducttoProductSubgroupView.class)) {
            nav.addItem(
                    new AppNavItem("Product to Product Subgroup", ProducttoProductSubgroupView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(SegmentsView.class)) {
            nav.addItem(new AppNavItem("Segments", SegmentsView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(ProductstoSegmentsView.class)) {
            nav.addItem(new AppNavItem("Products to Segments", ProductstoSegmentsView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(ProjectCategoryView.class)) {
            nav.addItem(new AppNavItem("Project Category", ProjectCategoryView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(NeedsCategoryView.class)) {
            nav.addItem(new AppNavItem("Needs Category", NeedsCategoryView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(NeedSubCategoryView.class)) {
            nav.addItem(new AppNavItem("Need SubCategory", NeedSubCategoryView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(NeedsCategorytoSegmentsView.class)) {
            nav.addItem(
                    new AppNavItem("Needs Category to Segments", NeedsCategorytoSegmentsView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(NeedsCategorytoProjectCategoryView.class)) {
            nav.addItem(new AppNavItem("Needs Category to Project Category", NeedsCategorytoProjectCategoryView.class,
                    "la la-columns"));

        }
        if (accessChecker.hasAccess(NeedSubCategorytoProductsView.class)) {
            nav.addItem(new AppNavItem("Need SubCategory to Products", NeedSubCategorytoProductsView.class,
                    "la la-columns"));

        }
        if (accessChecker.hasAccess(OpportunityCatalogView.class)) {
            nav.addItem(new AppNavItem("Opportunity Catalog", OpportunityCatalogView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(OpportunitySubCatalogView.class)) {
            nav.addItem(new AppNavItem("Opportunity SubCatalog", OpportunitySubCatalogView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(PortfolioTypeView.class)) {
            nav.addItem(new AppNavItem("Portfolio Type", PortfolioTypeView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(PortfolioSegmentView.class)) {
            nav.addItem(new AppNavItem("Portfolio Segment", PortfolioSegmentView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(TargetMeetingView.class)) {
            nav.addItem(new AppNavItem("Target Meeting", TargetMeetingView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(AdminView.class)) {
            nav.addItem(new AppNavItem("Admin", AdminView.class, "lab la-android"));

        }
        if (accessChecker.hasAccess(AboutView.class)) {
            nav.addItem(new AppNavItem("About", AboutView.class, "la la-file"));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
