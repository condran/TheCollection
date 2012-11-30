package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.BootstrapFeedbackPanel;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.Member;
import com.paulcondran.collection.model.ui.MemberNew;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("Admin")
public class MemberNewPage extends BasePage {

    private MemberNew memberNew = new MemberNew();
    private Form form;
    private FeedbackPanel feedbackPanel;


    public MemberNewPage() {

        setupUserInterfaceFields();
        
        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(memberNew, "member.organisation"), CollectionUtil.listOrganisations());

        Button saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(validateForm(target)) {
                    CollectionDatabase db =  CollectionDatabase.getInstance();

                    db.persist(memberNew.getMember());

                    getRequestCycle().setResponsePage(MemberSearchPage.class);
                }
            }
        };

        form.add(saveButton);
    }
    public void setEditMode(Member member) {
        memberNew.setMember(member);
    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);
        
        add(feedbackPanel = new BootstrapFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);


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

    private boolean validateForm(AjaxRequestTarget target) {
        boolean valid = true;
        if (StringUtils.isBlank(memberNew.getMember().getMemberID())) {
            error("Member ID is required");
            valid = false;
        }
        if (StringUtils.isBlank(memberNew.getMember().getName())) {
            error("Member Name is required");
            valid = false;
        }

        target.add(feedbackPanel);
        return valid;
    }
    /**
     * Queries the Member database to fill out the member details
     */
    private void performMemberQuery() {

    }

}
