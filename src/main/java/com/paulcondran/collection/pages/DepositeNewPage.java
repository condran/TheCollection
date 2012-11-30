package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.BootstrapFeedbackPanel;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.functional.Function1Void;
import com.paulcondran.collection.model.data.DepositItem;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.MonthlyDeposit;
import com.paulcondran.collection.model.ui.DepositNew;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;


import org.apache.wicket.markup.html.form.DropDownChoice;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("User")
public class DepositeNewPage extends BasePage {

    private DepositNew depositNew = new DepositNew();
    private Form form;
    private TextField totalField;
    private FeedbackPanel feedbackPanel;

    public DepositeNewPage() {

        setupUserInterfaceFields();
        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(depositNew, "deposit.organisation"), CollectionUtil.listOrganisations());
        DropDownChoice collector = addDropdownField("collector", 
                new PropertyModel<String>(depositNew, "deposit.collectorId"),  CollectionUtil.listEmptyList());
        organisation.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

//                if (depositNew.getDeposit().getOrganisation() != null) {
//                    if ("CL01".equals(depositNew.getDeposit().getCollectorId())) {
//                        organisation.setChoices(CollectionUtil.listOrganisations());
//                    }
//                    else {
//                        organisation.setChoices(CollectionUtil.listOrganisations2());
//                    }
//                } else {
//                    organisation.setChoices(CollectionUtil.listEmptyList());
//                }


                target.add(organisation);
            }
        });


    }

    public void setEditMode(MonthlyDeposit monthlyDeposit) {
        depositNew.setDeposit(monthlyDeposit);
        if (monthlyDeposit.getDepositList().size()>0) {
            depositNew.setAmount1(monthlyDeposit.getDepositList().get(0).getAmount());
            depositNew.setDate1(monthlyDeposit.getDepositList().get(0).getDate());
        }
        if (monthlyDeposit.getDepositList().size()>1) {
            depositNew.setAmount2(monthlyDeposit.getDepositList().get(1).getAmount());
            depositNew.setDate2(monthlyDeposit.getDepositList().get(1).getDate());
        }
        if (monthlyDeposit.getDepositList().size()>2) {
            depositNew.setAmount3(monthlyDeposit.getDepositList().get(2).getAmount());
            depositNew.setDate3(monthlyDeposit.getDepositList().get(2).getDate());
        }
        if (monthlyDeposit.getDepositList().size()>3) {
            depositNew.setAmount4(monthlyDeposit.getDepositList().get(3).getAmount());
            depositNew.setDate4(monthlyDeposit.getDepositList().get(3).getDate());
        }
        if (monthlyDeposit.getDepositList().size()>4) {
            depositNew.setAmount5(monthlyDeposit.getDepositList().get(4).getAmount());
            depositNew.setDate5(monthlyDeposit.getDepositList().get(4).getDate());
        }
        if (monthlyDeposit.getDepositList().size()>5) {
            depositNew.setAmount6(monthlyDeposit.getDepositList().get(5).getAmount());
            depositNew.setDate6(monthlyDeposit.getDepositList().get(5).getDate());
        }
        
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

//        for (DonationCategory donationCategory : donationNew.getDonation().getCategoryList()) {
//            categoryListView.add(new CategoryPanel(categoryListView.newChildId(), donationCategory,
//                    CollectionUtil.listCategories(), addCategoryFunction));
//        }
//
//        categoryListView.add(new CategoryPanel(categoryListView.newChildId(), null,
//                CollectionUtil.listCategories(), addCategoryFunction));
    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);

        add(feedbackPanel = new BootstrapFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);
        totalField = addTextField("total", new PropertyModel<String>(depositNew, "deposit.total"));
        totalField.setEnabled(false);

        // Second behaviour will match the selected item and populate the member fields.
        AjaxFormComponentUpdatingBehavior changeListner = new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                totalCategories();
                target.add(totalField);
            }
        };

        addTextField("depositPeriod", new PropertyModel<String>(depositNew, "deposit.depositPeriod"));
        addTextField("date1", new PropertyModel<String>(depositNew, "date1"));
        addTextField("date2", new PropertyModel<String>(depositNew, "date2"));
        addTextField("date3", new PropertyModel<String>(depositNew, "date3"));
        addTextField("date4", new PropertyModel<String>(depositNew, "date4"));
        addTextField("date5", new PropertyModel<String>(depositNew, "date5"));
        addTextField("date6", new PropertyModel<String>(depositNew, "date6"));

        TextField tf = addTextField("amount1", new PropertyModel<String>(depositNew, "amount1"));
        tf.add(changeListner);
        tf = addTextField("amount2", new PropertyModel<String>(depositNew, "amount2"));
//        tf.add(changeListner);
        tf = addTextField("amount3", new PropertyModel<String>(depositNew, "amount3"));
//        tf.add(changeListner);
        tf = addTextField("amount4", new PropertyModel<String>(depositNew, "amount4"));
//        tf.add(changeListner);
        tf = addTextField("amount5", new PropertyModel<String>(depositNew, "amount5"));
//        tf.add(changeListner);
        tf = addTextField("amount6", new PropertyModel<String>(depositNew, "amount6"));
//        tf.add(changeListner);
        
        Button saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(validateForm(target)) {
                    totalCategories();
                    MonthlyDeposit md = depositNew.getDeposit();
                    populateDepositeItems(md,  depositNew.getAmount1(), depositNew.getDate1());
                    populateDepositeItems(md,  depositNew.getAmount2(), depositNew.getDate2());
                    populateDepositeItems(md,  depositNew.getAmount3(), depositNew.getDate3());
                    populateDepositeItems(md,  depositNew.getAmount4(), depositNew.getDate4());
                    populateDepositeItems(md,  depositNew.getAmount5(), depositNew.getDate5());
                    populateDepositeItems(md,  depositNew.getAmount6(), depositNew.getDate6());
                    
                    CollectionDatabase db =  CollectionDatabase.getInstance();

                    db.persist(md);

                    getRequestCycle().setResponsePage(DepositeSearchPage.class);
                }
            }
        };
        form.add(saveButton);


    }

    private void populateDepositeItems(MonthlyDeposit md, BigDecimal amount, Date date) {
        if (amount != null &&
               amount.doubleValue() > 0) {
            DepositItem dItem = new DepositItem();
            dItem.setAmount(amount);
            dItem.setDate(date);
            dItem.setMonthlyDeposit(md);
            md.getDepositList().add(dItem);
        }

    }
    private boolean validateForm(AjaxRequestTarget target) {
        boolean valid = true;
        // Organisation
        if (StringUtils.isBlank(depositNew.getDeposit().getOrganisation())) {
            error("Organisation is required");
            valid = false;
        }
        // collector
        if (StringUtils.isBlank(depositNew.getDeposit().getCollectorId())) {
            error("Collector is required");
            valid = false;
        }
        if (depositNew.getDeposit().getDepositPeriod() == null) {
            error("Month is required");
            valid = false;
        }
        else
            if (depositNew.getDeposit().getDepositPeriod().length() != 7 ||
                    !depositNew.getDeposit().getDepositPeriod().substring(4, 5).equals("/")) {
                error("Month be in YYYY/MM format");
                valid = false;
            }
        
        // TODO check individual deposits (must have a date for non zero amounts)
        if (depositNew.getAmount1() != null && 
                depositNew.getAmount1().doubleValue()> 0 &&
                depositNew.getDate1() == null) {
                error("Deposit 1 must have a valid date");
                valid = false;
            
        }
        if (depositNew.getAmount2() != null && 
                depositNew.getAmount2().doubleValue()> 0 &&
                depositNew.getDate2() == null) {
                error("Deposit 2 must have a valid date");
                valid = false;
            
        }
        if (depositNew.getAmount3() != null && 
                depositNew.getAmount3().doubleValue()> 0 &&
                depositNew.getDate3() == null) {
                error("Deposit 3 must have a valid date");
                valid = false;
            
        }
        if (depositNew.getAmount4() != null && 
                depositNew.getAmount4().doubleValue()> 0 &&
                depositNew.getDate4() == null) {
                error("Deposit 4 must have a valid date");
                valid = false;
            
        }
        if (depositNew.getAmount5() != null && 
                depositNew.getAmount5().doubleValue()> 0 &&
                depositNew.getDate5() == null) {
                error("Deposit 5 must have a valid date");
                valid = false;
            
        }
        if (depositNew.getAmount6() != null && 
                depositNew.getAmount6().doubleValue()> 0 &&
                depositNew.getDate6() == null) {
                error("Deposit 6 must have a valid date");
                valid = false;
            
        }
        target.add(feedbackPanel);
        return valid;
    }

    private void totalCategories() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(depositNew.getAmount1()  == null ? BigDecimal.ZERO : depositNew.getAmount1());
        total = total.add(depositNew.getAmount2()  == null ? BigDecimal.ZERO : depositNew.getAmount2());
        total = total.add(depositNew.getAmount3()  == null ? BigDecimal.ZERO : depositNew.getAmount3());
        total = total.add(depositNew.getAmount4()  == null ? BigDecimal.ZERO : depositNew.getAmount4());
        total = total.add(depositNew.getAmount5()  == null ? BigDecimal.ZERO : depositNew.getAmount5());
        total = total.add(depositNew.getAmount6()  == null ? BigDecimal.ZERO : depositNew.getAmount6());
        depositNew.getDeposit().setTotal(total);
    }


}
