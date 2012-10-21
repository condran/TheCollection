package com.tekinsure.thecollection;

import com.tekinsure.thecollection.pages.DonationSearchPage;
import org.apache.wicket.Page;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.lang.PackageName;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 21/10/12
 * Time: 9:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionApplication extends WebApplication {


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


        // Mount the pages to paths
        mount(new MountedMapper("/donation.page", DonationSearchPage.class));
        //mount(new QueryStringUrlCodingStrategy("/search", TransactionSearch.class));

    }


}
