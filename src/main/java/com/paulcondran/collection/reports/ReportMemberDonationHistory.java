package com.paulcondran.collection.reports;

import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.*;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.data.DBUtil;
import com.paulcondran.collection.model.data.Category;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.DonationCategory;
import com.paulcondran.collection.model.data.Member;
import com.paulcondran.collection.model.ui.CategoryReportItem;
import com.paulcondran.collection.model.ui.OptionItem;
import com.paulcondran.collection.model.ui.ReportMemberHistory;
import com.paulcondran.collection.pages.BasePage;
import com.paulcondran.collection.pages.DonationNewPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The member donation history report
 */
public class ReportMemberDonationHistory extends BasePage {

    private Form form;

    private ReportMemberHistory reportFields = new ReportMemberHistory();

    private List<Donation> searchResults = new ArrayList<Donation>();
    private List<OptionItem> categories;
    private DataTable dataTable;
    private CollectionDataProvider<Donation> dataProvider;

    public ReportMemberDonationHistory() {

        categories = CollectionUtil.listCategories();
        setupUserInterfaceFields();
        setupResultsTable(null);

    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);

        // Create the member search field & add two behaviours.
        TextField memberSearch = addTextField("memberSearch", new PropertyModel<String>(reportFields, "memberSearch"));

        // First behaviour will perform the type-ahead lookups
        memberSearch.add(new BootstrapTypeAheadBehaviour() {

            public List<String> getChoices(String search) {
                return DBUtil.searchMembers(search);
            }
        });

        // Second behaviour will match the selected item and populate the member fields.
        memberSearch.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // Set the member for the search
                Member member = DBUtil.findMember(reportFields.getMemberSearch());
                if (member != null) {
                    reportFields.setMemberID(member.getMemberID());
                }

                // Update the Member label

            }
        });

        addTextField("dateFrom", new PropertyModel<String>(reportFields, "dateFrom"));
        addTextField("dateTo", new PropertyModel<String>(reportFields, "dateTo"));

        // Hook into the search behaviour
        form.add(new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                CollectionDatabase db = CollectionDatabase.getInstance();
                CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<Donation> query = builder.createQuery(Donation.class);
                Root<Donation> donationRoot = query.from(Donation.class);
                query.select(donationRoot);

                List<Predicate> predicateList = reportFields.listPredicates(donationRoot, builder);

                if (!predicateList.isEmpty()) {
                    Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
                    query.where(predicates);
                }

                searchResults = db.getEntityManager().createQuery(query).getResultList();
                setupResultsTable(target);

            }
        });
    }

    private List<CategoryReportItem> listCategoryItems() {
        List<OptionItem> categoryHeaders = new ArrayList<OptionItem>();
        List<CategoryReportItem> categoryReportItems = new ArrayList<CategoryReportItem>();
        OptionItem optionItem;

        for (Donation donation : searchResults) {
            for(DonationCategory donationCategory : donation.getCategoryList()) {
                String categoryCode = donationCategory.getCategoryName();
                if(!CollectionUtil.isOptionCodeInList(categoryCode, categoryHeaders)) {
                    categoryHeaders.add(optionItem = new OptionItem(categoryCode, CollectionUtil.findOption(categoryCode, categories).getDescription()));
                    categoryReportItems.add(new CategoryReportItem(categoryCode, optionItem.getDescription(), donationCategory.getAmount()));
                } else {
                    // add to total
                    for (CategoryReportItem categoryItem : categoryReportItems) {
                        if (categoryItem.getCategoryCode().equals(categoryCode))  {
                            categoryItem.addTotal(donationCategory.getAmount());
                        }
                    }
                }
            }
        }
        return categoryReportItems;
    }



    private void setupResultsTable(AjaxRequestTarget target) {

        List<IColumn> columns = new ArrayList<IColumn>();

        columns.add(new PropertyColumn<String, String>(new Model<String>("ReceiptNo"), "receiptNo", "receiptNo"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Date"), "date", "date"));

        // add a total row
        Donation totalRow = new Donation();
        totalRow.setReceiptNo("Total");
        searchResults.add(totalRow);

        List<CategoryReportItem> categoryHeaders = listCategoryItems();
        for (CategoryReportItem cat : categoryHeaders) {
            columns.add(new CategoryReportColumn(new Model<String>(cat.getCategoryName()), cat.getCategoryCode(), cat.getCategoryCode(),
                    "categoryList", "categoryName", "amount"));
            DonationCategory donationCategory = new DonationCategory();
            donationCategory.setAmount(cat.getCategoryTotal());
            donationCategory.setCategoryName(cat.getCategoryCode());
            totalRow.getCategoryList().add(donationCategory);
        }


        columns.add(new PropertyColumn<String, String>(new Model<String>("Total"), "total", "total") {
            @Override
            public IModel<Object> getDataModel(IModel<String> rowModel) {
                BigDecimal total = (BigDecimal)(new PropertyModel(rowModel, getPropertyExpression())).getObject();
                String fmtTotal = "";
                if (total != null) {
                    fmtTotal = DecimalFormat.getCurrencyInstance().format(total);
                }
                return new Model(fmtTotal);
            }
        });

        dataProvider = new CollectionDataProvider<Donation>(searchResults);

        DataTable newDataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        newDataTable.setOutputMarkupId(true);

        if (categoryHeaders.size() > 3) {
            String widthStyle = "width: 110%";

            if (categoryHeaders.size() > 5) {
                widthStyle = "width: 120%";
            }
            if (categoryHeaders.size() > 10) {
                widthStyle = "width: 150%";
            }
            if (categoryHeaders.size() > 20) {
                widthStyle = "width: 200%";
            }
            newDataTable.add(new AttributeModifier("style", widthStyle));
        }

        if(target == null) {
            dataTable = newDataTable;
            form.add(dataTable);
        } else {
            dataTable.replaceWith(newDataTable);
            dataTable = newDataTable;
            target.add(dataTable);
        }

    }

}
