package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.model.data.Donation;
import com.tekinsure.thecollection.model.ui.DonationNew;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DonationNewPage extends BasePage {

    private DonationNew donationNew = new DonationNew();

    public DonationNewPage() {

        setupUserInterfaceFields();
    }

    private void setupUserInterfaceFields() {

        addTextField("memberSearch", new PropertyModel<String>(donationNew, "donation.memberSearch"));
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
