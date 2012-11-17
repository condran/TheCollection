package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.BootstrapFeedbackPanel;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.CategoryDef;
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

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("admin")
public class CategoryNewPage extends BasePage {

    private CategoryDef categoryNew = new CategoryDef();
    private Form form;
    private FeedbackPanel feedbackPanel;

    public CategoryNewPage() {

        setupUserInterfaceFields();
        
        Button saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(validateForm(target)) {
                    
                    CollectionDatabase db =  CollectionDatabase.getInstance();
                    db.persist(categoryNew);
                    getRequestCycle().setResponsePage(CategorySearchPage.class);
                }
            }
        };
        form.add(saveButton);
    }
    public void setEditMode(CategoryDef category) {
        this.categoryNew = category;
    }
    private boolean validateForm(AjaxRequestTarget target) {
        boolean valid = true;
        if (StringUtils.isBlank(categoryNew.getCategoryID())) {
            error("Category ID is required");
            valid = false;
        }
        else if (alreadyExistsID(categoryNew.getCategoryID()))
        {
            error("Category ID already exists");
            valid = false;
            
        }
        if (StringUtils.isBlank(categoryNew.getName())) {
            error("category Name is required");
            valid = false;
        }
        else if (alreadyExistsName(categoryNew.getName()))
        {
            error("category Name already Exists");
            valid = false;
        }
        target.add(feedbackPanel);
        return valid;
    }

    private boolean alreadyExistsID(String id) {
        // TODO
        return false;
    }
    
    private boolean alreadyExistsName(String name) {
        // TODO
        return false;
    }
            
    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);


        addTextField("categoryID", new PropertyModel<String>(this, "categoryNew.categoryID"));
        addTextField("name", new PropertyModel<String>(this, "categoryNew.name"));
        addCheckboxField("promiseCategory", new PropertyModel<Boolean>(this, "categoryNew.promiseCategory"));
        addCheckboxField("belongsToGroup1", new PropertyModel<Boolean>(this, "categoryNew.belongsToGroup1"));
        addCheckboxField("belongsToGroup2", new PropertyModel<Boolean>(this, "categoryNew.belongsToGroup2"));

        add(feedbackPanel = new BootstrapFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

    }

    /**
     * Queries the Member database to fill out the member details
     */
    private void performMemberQuery() {

    }

}
