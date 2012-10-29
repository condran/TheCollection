package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.components.CollectionUtil;
import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.model.ui.MemberNew;
import java.util.Date;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.wicket.markup.html.form.DropDownChoice;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
public class MemberNewPage extends BasePage {

    private MemberNew memberNew = new MemberNew();
    private Form form;

    public MemberNewPage() {

        setupUserInterfaceFields();
        
        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(memberNew, "member.organisation"), CollectionUtil.listOrganisations());


        Button saveButton = new Button("save") {
            @Override
            public void onSubmit() {
                CollectionDatabase db =  CollectionDatabase.getInstance();

                db.persist(memberNew.getMember());

                getRequestCycle().setResponsePage(MemberSearchPage.class);
            }
        };
        form.add(saveButton);
    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);


        addTextField("memberID", new PropertyModel<String>(memberNew, "member.memberID"));
        addTextField("name", new PropertyModel<String>(memberNew, "member.name"));
        addTextField("directDebitRef", new PropertyModel<String>(memberNew, "member.directDebitRef"));
        addTextField("yearOfBirth", new PropertyModel<String>(memberNew, "member.yearOfBirth"));
        addTextField("dateFrom", new PropertyModel<Date>(memberNew, "member.dateFrom"));
        addTextField("dateTo", new PropertyModel<Date>(memberNew, "member.dateFrom"));
        addTextField("emailAddress", new PropertyModel<String>(memberNew, "member.emailAddress"));
        addTextField("mobileNo", new PropertyModel<String>(memberNew, "member.mobileNo"));
        addTextField("contactNo", new PropertyModel<String>(memberNew, "member.contactNo"));
        addTextField("address1", new PropertyModel<String>(memberNew, "member.address1"));
        addTextField("address2", new PropertyModel<String>(memberNew, "member.address2"));
        addTextField("suburb", new PropertyModel<String>(memberNew, "member.suburb"));
        addTextField("postcode", new PropertyModel<String>(memberNew, "member.postcode"));
        addTextField("state", new PropertyModel<String>(memberNew, "member.state"));

    }

    /**
     * Queries the Member database to fill out the member details
     */
    private void performMemberQuery() {

    }

}
