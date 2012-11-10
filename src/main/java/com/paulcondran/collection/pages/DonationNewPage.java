package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.BootstrapFeedbackPanel;
import com.paulcondran.collection.components.BootstrapTypeAheadBehaviour;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.functional.Function2Void;
import com.paulcondran.collection.model.data.Category;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.DonationCategory;
import com.paulcondran.collection.model.data.Member;
import com.paulcondran.collection.model.ui.DonationNew;
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
@AuthorizeInstantiation("user")
public class DonationNewPage extends BasePage {

    private DonationNew donationNew = new DonationNew();
    private Form form;
    private TextField totalField;
    private List<Category> availableCategories = new ArrayList<Category>();
    private FeedbackPanel feedbackPanel;
    private RepeatingView categoryListView;
    Function2Void<AjaxRequestTarget, DonationCategory> addCategoryFunction;

    public DonationNewPage() {

        setupUserInterfaceFields();
        final DropDownChoice organisation = addDropdownField("orgChapter",
                new PropertyModel<String>(donationNew, "donation.orgChapter"), CollectionUtil.listOrganisations());

        listCategories();

    }

    public void setEditMode(Donation donation) {
        Member member = findMember("["+donation.getMemberID()+"]");
        donationNew.setMember(member);
        donationNew.setDonation(donation);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        for (DonationCategory donationCategory : donationNew.getDonation().getCategoryList()) {
            categoryListView.add(new CategoryPanel(categoryListView.newChildId(), donationCategory,
                    CollectionUtil.listCategories(), addCategoryFunction));
        }

        categoryListView.add(new CategoryPanel(categoryListView.newChildId(), null,
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
        TextField memberSearch = addTextField("memberSearch", new PropertyModel<String>(donationNew, "memberSearch"));

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
                Member member = findMember(donationNew.getMemberSearch());

                if (member != null) {
                    donationNew.setMember(member);
                    if (StringUtils.isNotBlank(member.getMemberID())) {
                        donationNew.getDonation().setMemberID(member.getMemberID());
                    }
                    if (StringUtils.isNotBlank(member.getDirectDebitRef())) {
                        donationNew.getDonation().setDirectDebitRef(member.getDirectDebitRef());
                    }
                    if (StringUtils.isNotBlank(member.getOrganisation())) {
                        donationNew.getDonation().setOrgChapter(member.getOrganisation());
                    }
                    updateComponent(target, Arrays.asList("memberID", "ddRef", "address1", "address2", "suburb", "state", "orgChapter"));
                }
            }
        });

        addTextField("memberID", new PropertyModel<String>(donationNew, "donation.memberID"));
        addTextField("ddRef", new PropertyModel<String>(donationNew, "donation.directDebitRef"));
        addTextField("receiptNo", new PropertyModel<String>(donationNew, "donation.receiptNo"));
        totalField = addTextField("total", new PropertyModel<String>(donationNew, "donation.total"));
        totalField.setEnabled(false);
        addTextField("date", new PropertyModel<String>(donationNew, "donation.date"));
        addTextField("name", new PropertyModel<String>(donationNew, "donation.name"));
        addTextArea("details", new PropertyModel<String>(donationNew, "donation.details"));
        addTextField("address1", new PropertyModel<String>(donationNew, "member.address1"));
        addTextField("address2", new PropertyModel<String>(donationNew, "member.address2"));
        addTextField("suburb", new PropertyModel<String>(donationNew, "member.suburb"));
        addTextField("state", new PropertyModel<String>(donationNew, "member.state"));
        
        Button saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(validateForm(target)) {
                    CollectionDatabase db =  CollectionDatabase.getInstance();

                    db.persist(donationNew.getDonation());

                    getRequestCycle().setResponsePage(DonationSearchPage.class);
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


        addCategoryFunction = new Function2Void<AjaxRequestTarget, DonationCategory>() {
            @Override
            public void apply(AjaxRequestTarget target, DonationCategory donationCategory) {
                // This executes the add method
                if (donationCategory.getAmount() != null && !donationCategory.getAmount().equals(BigDecimal.ZERO)) {

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

    }

    private boolean validateForm(AjaxRequestTarget target) {
        boolean valid = true;
        if (StringUtils.isBlank(donationNew.getDonation().getMemberID())) {
            error("Member ID is required");
            valid = false;
        }
        if (StringUtils.isBlank(donationNew.getDonation().getName())) {
            error("Member Name is required");
            valid = false;
        }
        if (donationNew.getDonation().getDate() == null) {
            error("Date is required");
            valid = false;
        }
        if (donationNew.getDonation().getCategoryList().isEmpty()) {
            error("One or more category entries are required");
            valid = false;
        }

        target.add(feedbackPanel);
        return valid;
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
        return (Member) q.getResultList().get(0);
    }

}
