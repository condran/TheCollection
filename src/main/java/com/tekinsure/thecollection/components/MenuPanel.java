package com.tekinsure.thecollection.components;

import com.tekinsure.thecollection.UIConstants;
import com.tekinsure.thecollection.functional.Function1Void;
import com.tekinsure.thecollection.pages.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.component.IRequestablePage;

import java.util.Arrays;
import java.util.List;

/**
 * Common menu panel
 */
public class MenuPanel extends BasePanel {

    List<String> userPages = Arrays.asList("DonationSearchPage", "PromisesSearchPage", "UserSettingsPage");
    List<String> adminPages = Arrays.asList("usersPage", "membersPage", "importDonations", "importMembers");


    public MenuPanel(String id) {
        super(id);

        RepeatingView navList = new RepeatingView("navList");
        add(navList);

        Roles roles = getSessionBean().getRoles();

        if (roles.hasRole(UIConstants.USER_ROLE)) {
            addUserMenu(navList);
        }

        if (roles.hasRole(UIConstants.ADMIN_ROLE)) {
            addAdminMenu(navList);
        }

    }


    private void addUserMenu(RepeatingView navList) {
        navList.add(new NavHeader(navList.newChildId(), "Actions Menu"));

        navList.add(new NavItem(navList.newChildId(), "Donations", active(Arrays.asList("DonationSearchPage", "DonationNewPage")),
                redirectFunc(DonationSearchPage.class)));

//        navList.add(new NavItem(navList.newChildId(), "Promises", active(Arrays.asList("PromiseSearchPage", "PromiseNewPage")),
//                redirectFunc(DonationSearchPage.class)));

//        navList.add(new NavItem(navList.newChildId(), "User Settings", active(Arrays.asList("UserSettingsPage")),
//                redirectFunc(DonationSearchPage.class)));

        navList.add(new NavItem(navList.newChildId(), "Logout", false, new Function1Void<AjaxRequestTarget>() {

            @Override
            public void apply(AjaxRequestTarget arg1) {
                IAuthenticationStrategy strategy = getApplication().getSecuritySettings()
                        .getAuthenticationStrategy();

                strategy.remove();

                getSession().invalidate();
                getRequestCycle().setResponsePage(LoginPage.class);
            }
        }));
    }

    private void addAdminMenu(RepeatingView navList) {
        navList.add(new NavHeader(navList.newChildId(), "Admin Menu"));

//        navList.add(new NavItem(navList.newChildId(), "Users", active(Arrays.asList("UserSearchPage", "UserNewPage")),
//                redirectFunc(DonationSearchPage.class)));

        navList.add(new NavItem(navList.newChildId(), "Members", active(Arrays.asList("MemberSearchPage", "MemberNewPage")),
                redirectFunc(MemberSearchPage.class)));

        navList.add(new NavItem(navList.newChildId(), "Import Donations", active(Arrays.asList("ImportDonationsPage")),
                redirectFunc(ImportDonationsPage.class)));

        navList.add(new NavItem(navList.newChildId(), "Import Members", active(Arrays.asList("ImportMembersPage")),
                redirectFunc(ImportMembersPage.class)));

    }

    /**
     * Determines if the menu page is the active one
     * @param names
     * @return
     */
    private boolean active(List<String> names) {
        boolean active = false;
        for (String name : names) {
            if (getSessionBean().getActivePageName().startsWith(name)) {
                active = true;
            }
        }
        return active;
    }

    /**
     * Assistant method to redirect to a particular page
     *
     * @param pageClass
     * @return
     */
    private Function1Void<AjaxRequestTarget> redirectFunc(final Class<? extends IRequestablePage> pageClass) {
        return new Function1Void<AjaxRequestTarget>() {

            @Override
            public void apply(AjaxRequestTarget arg1) {
                getRequestCycle().setResponsePage(pageClass);
            }
        };
    }


}
