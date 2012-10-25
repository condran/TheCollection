package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.model.data.Category;
import com.tekinsure.thecollection.model.data.Donation;
import com.tekinsure.thecollection.model.data.DonationCategory;
import com.tekinsure.thecollection.model.ui.DonationNew;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
public class DonationNewPage extends BasePage {

    private DonationNew donationNew = new DonationNew();
    private Form form;
    private List<Category> availableCategories = new ArrayList<Category>();

    public DonationNewPage() {

        setupUserInterfaceFields();

        listCategories();

    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);


        addTextField("memberSearch", new PropertyModel<String>(donationNew, "memberSearch"));
        addTextField("memberID", new PropertyModel<String>(donationNew, "donation.memberID"));
        addTextField("orgChapter", new PropertyModel<String>(donationNew, "donation.orgChapter"));
        addTextField("ddRef", new PropertyModel<String>(donationNew, "donation.directDebitRef"));
        addTextField("receiptNo", new PropertyModel<String>(donationNew, "donation.receiptNo"));
        addTextField("total", new PropertyModel<String>(donationNew, "donation.total"));
        addTextField("date", new PropertyModel<String>(donationNew, "donation.date"));
        addTextField("address1", new PropertyModel<String>(donationNew, "member.address1"));
        addTextField("address2", new PropertyModel<String>(donationNew, "member.address2"));
        addTextField("suburb", new PropertyModel<String>(donationNew, "member.suburb"));
        addTextField("state", new PropertyModel<String>(donationNew, "member.state"));

        Button saveButton = new Button("save") {
            @Override
            public void onSubmit() {
                CollectionDatabase db =  CollectionDatabase.getInstance();

                db.persist(donationNew.getDonation());

                getRequestCycle().setResponsePage(DonationSearchPage.class);
            }
        };
        form.add(saveButton);


        // Category repeater
        RepeatingView categoryListView = new RepeatingView("categoryList");
        categoryListView.newChildId();
    }

    private void listCategories() {
        CollectionDatabase db = CollectionDatabase.getInstance();
        Query q = db.getEntityManager().createQuery("from Category");

        List<Category> categories = q.getResultList();

        availableCategories.clear();
        for (Category category : categories) {
            if (!categoryInDonation(category.getCode())) {
                availableCategories.add(category);
            }
        }
    }

    private boolean categoryInDonation(String categoryCode) {
        for (DonationCategory donationCategory : donationNew.getDonation().getCategoryList()) {
            if (donationCategory.getCategoryName().equals(categoryCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Queries the Member database to fill out the member details
     */
    private void performMemberQuery() {

    }

}
