package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.model.data.Donation;
import com.tekinsure.thecollection.model.ui.DonationNew;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.EntityTransaction;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DonationNewPage extends BasePage {

    private DonationNew donationNew = new DonationNew();
    private Form form;

    public DonationNewPage() {

        setupUserInterfaceFields();

        Button saveButton = new Button("save") {
            @Override
            public void onSubmit() {
                CollectionDatabase.getInstance().connectDatabase();
                EntityTransaction transaction = CollectionDatabase.getInstance().getEntityManager().getTransaction();
                transaction.begin();

                CollectionDatabase.getInstance().getEntityManager().persist(donationNew.getDonation());

                transaction.commit();
            }
        };
        form.add(saveButton);
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

    }
}
