package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.components.CollectionDataTable;
import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.model.data.Donation;
import com.tekinsure.thecollection.model.ui.DonationSearch;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Paul Condran
 */
public class DonationSearchPage extends BasePage {

    private DonationSearch donationSearch = new DonationSearch();

    private List<Donation> searchResults = null;

    private DataTable dataTable;

    public DonationSearchPage() {

        setupUserInterfaceFields();

        searchResults = recentDonations();
        setupResultsTable();

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
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from Donation order by date desc");
        q.setMaxResults(10);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            donationList = list;
        }

        return donationList;
    }

    /**
     * Performs the search for donations
     */
    private void performDonationSearch() {

    }

    private void setupResultsTable() {

        List<IColumn> columns = new ArrayList<IColumn>();

        columns.add(new PropertyColumn(new Model<String>("Name"), "name", "name"));
        columns.add(new PropertyColumn(new Model<String>("Member"), "memberID", "memberID"));
        columns.add(new PropertyColumn(new Model<String>("Record No"), "receiptNo", "receiptNo"));
        columns.add(new PropertyColumn(new Model<String>("Total"), "total", "total"));

        SortableDataProvider dataProvider = new SortableDataProvider() {
            @Override
            public Iterator iterator(long l, long l1) {
                List newList = new ArrayList();
                for (long i = l; i < l1; i++) {
                    newList.add(searchResults.get((int)i));
                }
                return newList.iterator();
            }

            @Override
            public long size() {
                return searchResults.size();
            }

            @Override
            public IModel model(Object o) {
                return new Model((Donation)o);
            }
        };

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, 5);
        add(dataTable);

    }
}
