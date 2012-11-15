package com.paulcondran.collection;

import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.pages.*;
import com.paulcondran.collection.reports.ReportMemberDonationHistory;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;

/**
 * The wicket application configuration for The Collection site
 */
public class CollectionApplication extends AuthenticatedWebApplication {


    @Override
    public Class<? extends Page> getHomePage() {
        return DonationSearchPage.class;
    }

    @Override
    protected void init() {
        super.init();

        // Initialize resource settings
        WebResourceLocator locator = new WebResourceLocator();
        getResourceSettings().setResourceStreamLocator(locator);
        //getResourceSettings().addResourceFolder(PATH_PAGES);

        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

        getMarkupSettings().setStripWicketTags(true);

        // Mount the pages to paths
        //mount(new QueryStringUrlCodingStrategy("/search", TransactionSearch.class));
        mount(new MountedMapper("/DonationSearchPage.html", DonationSearchPage.class));
        mount(new MountedMapper("/DonationNewPage.html", DonationNewPage.class));
        mount(new MountedMapper("/MemberSearchPage.html", MemberSearchPage.class));
        mount(new MountedMapper("/MemberNewPage.html", MemberNewPage.class));
        mount(new MountedMapper("/PromiseSearchPage.html", PromiseSearchPage.class));
        mount(new MountedMapper("/PromiseNewPage.html", PromiseNewPage.class));
        mount(new MountedMapper("/UserSearchPage.html", UserSearchPage.class));
        mount(new MountedMapper("/UserNewPage.html", UserNewPage.class));
        mount(new MountedMapper("/ImportDonationsPage.html", ImportDonationsPage.class));
        mount(new MountedMapper("/ImportMembersPage.html", ImportMembersPage.class));
        mount(new MountedMapper("/ReportMemberDonationHistory.html", ReportMemberDonationHistory.class));
        mount(new MountedMapper("/index.html", LoginPage.class));

    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return SessionBean.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CollectionDatabase.getInstance().shutdown();
    }
}
