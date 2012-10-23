package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.model.data.Donation;
import com.tekinsure.thecollection.model.ui.DonationSearch;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Condran
 */
public class DonationSearchPage extends BasePage {

    private DonationSearch donationSearch = new DonationSearch();

    public DonationSearchPage() {

        setupUserInterfaceFields();

        recentDonations();

    }

    /**
     * This method creates the Wicket user interface fields and binds to the model object.
     */
    private void setupUserInterfaceFields() {

        addTextField("memberID", new PropertyModel<String>(donationSearch, "memberID"));
        addTextField("name", new PropertyModel<String>(donationSearch, "name"));
        addTextField("receipt", new PropertyModel<String>(donationSearch, "receipt"));
        addTextField("ddt", new PropertyModel<String>(donationSearch, "ddt"));
        addTextField("dateFrom", new PropertyModel<String>(donationSearch, "dateFrom"));
        addTextField("dateTo", new PropertyModel<String>(donationSearch, "dateTo"));

    }


    private List<Donation> recentDonations() {
        List<Donation> donationList = new ArrayList<Donation>();
        CollectionDatabase.getInstance().connectDatabase();
        EntityManager em = CollectionDatabase.getInstance().getEntityManager();

        Query q = em.createQuery("from Donation order by date desc");
        q.setMaxResults(5);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            donationList = list;
        }

        return donationList;
    }
}
