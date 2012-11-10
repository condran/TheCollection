package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.BootstrapFeedbackPanel;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.User;
import com.paulcondran.collection.model.ui.UserNew;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.form.PasswordTextField;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("admin")
public class UserNewPage extends BasePage {

    private UserNew userNew = new UserNew();
    private Form form;
    private FeedbackPanel feedbackPanel;

    public UserNewPage() {

        setupUserInterfaceFields();
        
        final DropDownChoice userType = addDropdownField("type",
                new PropertyModel<String>(userNew, "user.type"), CollectionUtil.listUserTypes());

        Button saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(validateForm(target)) {
                    userNew.getUser().setPassword(userNew.getPassword());
                    CollectionDatabase db =  CollectionDatabase.getInstance();
                    db.persist(userNew.getUser());
                    getRequestCycle().setResponsePage(UserSearchPage.class);
                }
            }
        };
        form.add(saveButton);
    }
    public void setEditMode(User user) {
        userNew.setUser(user);
    }
    private boolean validateForm(AjaxRequestTarget target) {
        boolean valid = true;
        if (StringUtils.isBlank(userNew.getUser().getUserID())) {
            error("User ID is required");
            valid = false;
        }
        if (StringUtils.isBlank(userNew.getUser().getName())) {
            error("User Name is required");
            valid = false;
        }
        if (StringUtils.isBlank(userNew.getUser().getType())) {
            error("Type is required");
            valid = false;
        }
        if (StringUtils.isBlank(userNew.getUser().getEmailAddress())) {
            error("EmailAddress is required");
            valid = false;
        }

        if (StringUtils.isBlank(userNew.getUser().getMobileNo())) {
            error("MobileNo is required");
            valid = false;
        }
        if (StringUtils.isBlank(userNew.getPassword()) || !StringUtils.equals(userNew.getPassword(), userNew.getPassverify())) {
            error("Password verification fails, please ensure the two passwords match");
            valid = false;
        }
        target.add(feedbackPanel);
        return valid;
    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);


        addTextField("userID", new PropertyModel<String>(userNew, "user.userID"));
        addTextField("name", new PropertyModel<String>(userNew, "user.name"));
        addTextField("emailAddress", new PropertyModel<String>(userNew, "user.emailAddress"));
        addTextField("mobileNo", new PropertyModel<String>(userNew, "user.mobileNo"));
        
        addPasswordText("password", new PropertyModel<String>(userNew, "password"));
        addPasswordText("passverify", new PropertyModel<String>(userNew, "passverify"));

        add(feedbackPanel = new BootstrapFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

    }

    /**
     * Queries the Member database to fill out the member details
     */
    private void performMemberQuery() {

    }

}
