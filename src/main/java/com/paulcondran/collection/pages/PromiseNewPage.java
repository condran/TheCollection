package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.BootstrapFeedbackPanel;
import com.paulcondran.collection.components.BootstrapTypeAheadBehaviour;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.functional.Function2Void;
import com.paulcondran.collection.model.data.Category;
import com.paulcondran.collection.model.data.DonationCategory;
import com.paulcondran.collection.model.data.Member;
import com.paulcondran.collection.model.data.Promise;
import com.paulcondran.collection.model.data.PromiseCategory;
import com.paulcondran.collection.model.ui.PromiseNew;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.Query;

import org.apache.wicket.markup.html.form.DropDownChoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("User")
public class PromiseNewPage extends BasePage {

    private PromiseNew promiseNew = new PromiseNew();
    private Form form;
    private TextField totalField;
    private List<Category> availableCategories = new ArrayList<Category>();
    private FeedbackPanel feedbackPanel;
    private RepeatingView categoryListView;
    Function2Void<AjaxRequestTarget, PromiseCategory> addCategoryFunction;

    public PromiseNewPage() {

        setupUserInterfaceFields();
        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(promiseNew, "promise.organisation"), CollectionUtil.listOrganisations());

        listCategories();

    }

    public void setEditMode(Promise promise) {
        Member member = findMember("["+promise.getMemberID()+"]");
        promiseNew.setMember(member);
        promiseNew.setPromise(promise);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        for (PromiseCategory promiseCategory : promiseNew.getPromise().getCategoryList()) {
            categoryListView.add(new PromiseCategoryPanel(categoryListView.newChildId(), promiseCategory,
                    CollectionUtil.listPromiseCategories(), addCategoryFunction));
        }

        categoryListView.add(new PromiseCategoryPanel(categoryListView.newChildId(), null,
                CollectionUtil.listCategories(), addCategoryFunction));
    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);

        add(feedbackPanel = new BootstrapFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

        // Create the member search field & add two behaviours.
        TextField memberSearch = addTextField("memberSearch", new PropertyModel<String>(promiseNew, "memberSearch"));

        // First behaviour will perform the type-ahead lookups
        memberSearch.add(new BootstrapTypeAheadBehaviour() {

            public List<String> getChoices(String search) {
                return searchMembers(search);
            }
        });

        // Second behaviour will match the selected item and populate the member fields.
        memberSearch.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // Populate the member fields
                Member member = findMember(promiseNew.getMemberSearch());

                if (member != null) {
                    promiseNew.setMember(member);
                    if (StringUtils.isNotBlank(member.getMemberID())) {
                        promiseNew.getPromise().setMemberID(member.getMemberID());
                    }
                    if (StringUtils.isNotBlank(member.getDirectDebitRef())) {
                        promiseNew.getPromise().setDirectDebitRef(member.getDirectDebitRef());
                    }
                    
                    if (StringUtils.isNotBlank(member.getName())) {
                        promiseNew.getPromise().setName(member.getName());
                    }

                    if (StringUtils.isNotBlank(member.getOrganisation())) {
                        promiseNew.getPromise().setOrganisation(member.getOrganisation());
                    }
                    updateComponent(target, Arrays.asList("memberID", "name", "ddRef", "organisation", "address1", "address2", "suburb", "state"));
                }
            }
        });

        addTextField("memberID", new PropertyModel<String>(promiseNew, "promise.memberID"));
        addTextField("name", new PropertyModel<String>(promiseNew, "promise.name"));
        addTextField("ddRef", new PropertyModel<String>(promiseNew, "promise.directDebitRef"));
        addTextField("financialYear", new PropertyModel<String>(promiseNew, "promise.financialYear"));
        totalField = addTextField("total", new PropertyModel<String>(promiseNew, "promise.total"));
        totalField.setEnabled(false);
        TextField txtField = addTextField("address1", new PropertyModel<String>(promiseNew, "member.address1"));
        txtField.setEnabled(false);
        txtField = addTextField("address2", new PropertyModel<String>(promiseNew, "member.address2"));
        txtField.setEnabled(false);
        txtField = addTextField("suburb", new PropertyModel<String>(promiseNew, "member.suburb"));
        txtField.setEnabled(false);
        txtField = addTextField("state", new PropertyModel<String>(promiseNew, "member.state"));
        txtField.setEnabled(false);
        
        Button saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(validateForm(target)) {
                    CollectionDatabase db =  CollectionDatabase.getInstance();

                    db.persist(promiseNew.getPromise());

                    getRequestCycle().setResponsePage(PromiseSearchPage.class);
                }
            }
        };
        form.add(saveButton);


        // Category repeater
        categoryListView = new RepeatingView("categoryList");
        categoryListView.setOutputMarkupId(true);

        final WebMarkupContainer categoryListContainer = new WebMarkupContainer("categoryListContainer");
        categoryListContainer.setOutputMarkupId(true);
        categoryListContainer.add(categoryListView);
        form.add(categoryListContainer);


        addCategoryFunction = new Function2Void<AjaxRequestTarget, PromiseCategory>() {
            @Override
            public void apply(AjaxRequestTarget target, PromiseCategory promiseCategory) {
                // This executes the add method
                if (promiseCategory.getAmount() != null && !promiseCategory.getAmount().equals(BigDecimal.ZERO)) {

                    if (!promiseNew.getPromise().getCategoryList().contains(promiseCategory)) {
                        promiseNew.getPromise().getCategoryList().add(promiseCategory);
                        promiseCategory.setPromise(promiseNew.getPromise());
                        categoryListView.add(new PromiseCategoryPanel(categoryListView.newChildId(), null, CollectionUtil.listPromiseCategories(), this));
                        totalCategories();
                        target.add(totalField);
                        target.add(categoryListContainer);
                    }
                }
            }
        };

    }

    private boolean validateForm(AjaxRequestTarget target) {
        boolean valid = true;
        if (StringUtils.isBlank(promiseNew.getPromise().getMemberID())) {
            error("Member ID is required");
            valid = false;
        }
        if (StringUtils.isBlank(promiseNew.getPromise().getName())) {
            error("Member Name is required");
            valid = false;
        }
        if (promiseNew.getPromise().getFinancialYear() == null) {
            error("FinYear is required");
            valid = false;
        }
        if (promiseNew.getPromise().getCategoryList().isEmpty()) {
            error("One or more promise category entries are required");
            valid = false;
        }

        target.add(feedbackPanel);
        return valid;
    }

    private void totalCategories() {
        BigDecimal total = BigDecimal.ZERO;
        for (PromiseCategory dc : promiseNew.getPromise().getCategoryList()) {
            if (dc.getAmount() != null) {
                total = total.add(dc.getAmount());
            }
        }
        promiseNew.getPromise().setTotal(total);
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
        for (PromiseCategory promiseCategory : promiseNew.getPromise().getCategoryList()) {
            if (promiseCategory.getCategoryName().equals(categoryCode)) {
                return true;
            }
        }
        return false;
    }

    private List<String> searchMembers(String search) {
        List<String> results = new ArrayList<String>();

        List<Member> members = performMemberQuery(search);
        if (members != null) {
            for (Member member : members) {
                StringBuilder result = new StringBuilder();

                CollectionUtil.appendIfNotBlank(result, member.getName(), "%s-");
                CollectionUtil.appendIfNotBlank(result, member.getFamilyName(), "%s-");
                CollectionUtil.appendIfNotBlank(result, member.getSuburb(), "%s-");
                CollectionUtil.appendIfNotBlank(result, member.getMemberID(), "[%s]");

                results.add(result.toString());
            }
        }

        return results;
    }


    /**
     * Queries the Member database to fill out the member details
     */
    private List<Member> performMemberQuery(String search) {
        
        try {
       

        CollectionDatabase db = CollectionDatabase.getInstance();
        String query = "from Member where memberID='"+search+"' or name like '%"+search+"%'";
        Query q = db.getEntityManager().createQuery(query);

        return q.getResultList();
        
        } catch (Exception ee)
        {
            ee.printStackTrace();
            return null;
        }
    }

    /**
     * Finds the exact member based on type ahead query
     * @param memberSearch
     * @return
     */
    private Member findMember(String memberSearch) {

        if (memberSearch == null) { return null;}
        String memberID = StringUtils.substringBetween(memberSearch, "[", "]");
        if (memberID == null || StringUtils.isEmpty(memberID)) { return null; }
        CollectionDatabase db = CollectionDatabase.getInstance();
        Query q = db.getEntityManager().createQuery("from Member where memberID='"+memberID+"'");

        if (q.getResultList().isEmpty()) {
            return null;
        }
        return (Member) q.getResultList().get(0);
    }

}
