package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.functional.Function1Void;
import com.tekinsure.thecollection.functional.Function2Void;
import com.tekinsure.thecollection.model.data.Category;
import com.tekinsure.thecollection.model.data.DonationCategory;
import com.tekinsure.thecollection.model.ui.DonationNew;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



import com.tekinsure.thecollection.components.CollectionUtil;
import org.apache.wicket.markup.html.form.DropDownChoice;



/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
public class DonationNewPage extends BasePage {

    private DonationNew donationNew = new DonationNew();
    private Form form;
    private List<Category> availableCategories = new ArrayList<Category>();
    private TextField totalField;

    public DonationNewPage() {

        setupUserInterfaceFields();

        final DropDownChoice organisation = addDropdownField("orgChapter",
                new PropertyModel<String>(donationNew, "donation.orgChapter"), CollectionUtil.listOrganisations());

        listCategories();

    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);


        addTextField("memberSearch", new PropertyModel<String>(donationNew, "memberSearch"));
        addTextField("memberID", new PropertyModel<String>(donationNew, "donation.memberID"));
        addTextField("ddRef", new PropertyModel<String>(donationNew, "donation.directDebitRef"));
        addTextField("receiptNo", new PropertyModel<String>(donationNew, "donation.receiptNo"));
        totalField = addTextField("total", new PropertyModel<String>(donationNew, "donation.total"));
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
        final RepeatingView categoryListView = new RepeatingView("categoryList");
        categoryListView.setOutputMarkupId(true);

        final WebMarkupContainer categoryListContainer = new WebMarkupContainer("categoryListContainer");
        categoryListContainer.setOutputMarkupId(true);
        categoryListContainer.add(categoryListView);
        form.add(categoryListContainer);


        Function2Void<AjaxRequestTarget, DonationCategory> addFunction = new Function2Void<AjaxRequestTarget, DonationCategory>() {
            @Override
            public void apply(AjaxRequestTarget target, DonationCategory donationCategory) {
                // This executes the add method
                if (donationCategory.getAmount() != null || !donationCategory.getAmount().equals(BigDecimal.ZERO)) {

                    if (!donationNew.getDonation().getCategoryList().contains(donationCategory)) {
                        donationNew.getDonation().getCategoryList().add(donationCategory);
                        categoryListView.add(new CategoryPanel(categoryListView.newChildId(), null, CollectionUtil.listCategories(), this));
                        totalCategories();
                        target.add(totalField);
                        target.add(categoryListContainer);
                    }
                }
            }
        };

        categoryListView.add(new CategoryPanel(categoryListView.newChildId(), null, CollectionUtil.listCategories(), addFunction));

    }

    private void totalCategories() {
        BigDecimal total = BigDecimal.ZERO;
        for (DonationCategory dc : donationNew.getDonation().getCategoryList()) {
            if (dc.getAmount() != null) {
                total = total.add(dc.getAmount());
            }
        }
        donationNew.getDonation().setTotal(total);
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

