package com.paulcondran.collection.pages;

import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.CollectionDataProvider;
import com.paulcondran.collection.components.CollectionDataTable;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.components.ViewEditDelColumn;
import com.paulcondran.collection.data.CSVResourceStream;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.DonationCategory;
import com.paulcondran.collection.model.ui.DonationSearch;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.time.Duration;
import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Paul Condran
 */
@AuthorizeInstantiation("user")
public class DonationSearchPage extends BasePage {

    private DonationSearch donationSearch = new DonationSearch();

    private List<Donation> searchResults = null;

    private Form form;

    private DataTable dataTable;

    private CSVResourceStream csvResourceStream;

    public DonationSearchPage() {

        setupUserInterfaceFields();

        searchResults = recentDonations();

        setupResultsTable();

    }

    /**
     * This method creates the Wicket user interface fields and binds to the model object.
     */
    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);


        addTextField("memberID", new PropertyModel<String>(donationSearch, "memberID"));
        addTextField("name", new PropertyModel<String>(donationSearch, "name"));
        addTextField("receipt", new PropertyModel<String>(donationSearch, "receipt"));
        addTextField("ddt", new PropertyModel<String>(donationSearch, "ddt"));
        addTextField("dateFrom", new PropertyModel<String>(donationSearch, "dateFrom"));
        addTextField("dateTo", new PropertyModel<String>(donationSearch, "dateTo"));

        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(donationSearch, "organisation"), CollectionUtil.listEmptyList());


        DropDownChoice collector = addDropdownField("collector", new PropertyModel<String>(donationSearch, "collector"),
                CollectionUtil.listCollectors());
        // Logic to determine list based on collector
        collector.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                if (donationSearch.getCollector() != null) {
                    if ("CL01".equals(donationSearch.getCollector())) {
                        organisation.setChoices(CollectionUtil.listOrganisations());
                    }
                    else {
                        organisation.setChoices(CollectionUtil.listOrganisations2());
                    }
                } else {
                    organisation.setChoices(CollectionUtil.listEmptyList());
                }


                target.add(organisation);
            }
        });



        // Hook into the search behaviour
        form.add(new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                CollectionDatabase db = CollectionDatabase.getInstance();
                CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<Donation> query = builder.createQuery(Donation.class);
                Root<Donation> donationRoot = query.from(Donation.class);
                query.select(donationRoot);

                List<Predicate> predicateList = donationSearch.listPredicates(donationRoot, builder);

                if (!predicateList.isEmpty()) {
                    Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
                    query.where(predicates);
                }

                searchResults = db.getEntityManager().createQuery(query).getResultList();
                target.add(dataTable);


            }
        });

        addCSVExportControls();
    }

    /**
     * CSV export link and supporting code
     */
    private void addCSVExportControls() {

        csvResourceStream = new CSVResourceStream() {
            @Override
            public List<List<Object>> getObjectsList() {
                List<List<Object>> objectsList = new ArrayList<List<Object>>();
                List<String> donationCategoryHeaders = getDonationCategoryHeaders();

                for (Donation donation : searchResults) {
                    List<Object> donationObjects = new ArrayList<Object>();
                    donationObjects.add(donation.getMemberID());
                    donationObjects.add(donation.getName());
                    donationObjects.add(donation.getReceiptNo());
                    donationObjects.add(donation.getDirectDebit());
                    donationObjects.add(donation.getDirectDebitRef());
                    donationObjects.add(donation.getCollector());
                    donationObjects.add(donation.getOrgChapter());
                    donationObjects.add(donation.getDate());
                    donationObjects.add(donation.getTotal());
                    donationObjects.add(donation.getDetails());
                    for (String categoryHeader : donationCategoryHeaders) {
                        boolean itemAdded = false;
                        for (DonationCategory donationCategory : donation.getCategoryList()) {
                            if (categoryHeader.equals(donationCategory.getCategoryName())) {
                                donationObjects.add(donationCategory.getAmount());
                                itemAdded = true;
                            }
                        }
                        if (!itemAdded) {
                            donationObjects.add(null);
                        }
                    }

                    objectsList.add(donationObjects);
                }
                return objectsList;
            }

            @Override
            public CellProcessor[] getCellProcessors() {
                List<CellProcessor> processorList = new ArrayList<CellProcessor>();
                processorList.add(new NotNull());             // memberID
                processorList.add(new Optional());            //name
                processorList.add(new NotNull());             // recieptNo
                processorList.add(new FmtBool("Y","N"));      // isDirectDebit
                processorList.add(new Optional());            // directDebitRef
                processorList.add(new Optional());            // collector
                processorList.add(new Optional());            // orgChapter
                processorList.add(new FmtDate("dd/MM/yyyy")); // date
                processorList.add(new FmtNumber((DecimalFormat)DecimalFormat.getCurrencyInstance())); // total
                processorList.add(new Optional());
                int headersLength = getDonationCategoryHeaders().size();
                for (int i=0; i < headersLength; i++) {
                    // Donation Category amount fields
                    processorList.add(new FmtNumber((DecimalFormat)DecimalFormat.getCurrencyInstance()));
                }

                return processorList.toArray(new CellProcessor[processorList.size()]);
            }

            @Override
            public String[] getHeaders() {
                List<String> csvHeaders = getCSVHeaders();
                return csvHeaders.toArray(new String[csvHeaders.size()]);
            }
        };
        ResourceStreamResource resourceStream =new ResourceStreamResource(csvResourceStream);
        resourceStream.setContentDisposition(ContentDisposition.ATTACHMENT);
        resourceStream.setFileName("Donations.csv");
        resourceStream.setCacheDuration(Duration.NONE);

        form.add(new ResourceLink("csvExport", resourceStream));
    }

    /**
     * Gets a dynamic list of the headers for CSV export
     * @return
     */
    protected List<String> getCSVHeaders() {
        List<String> headers = new ArrayList<String>();
        headers.addAll(Arrays.asList("memberID", "name", "receiptNo", "directDebit", "directDebitRef",
                "collector", "orgChapter", "date", "total", "details"));
        headers.addAll(getDonationCategoryHeaders());
        return headers;
    }

    /**
     * Gets a list of unique categories across the search results
     *
     * @return
     */
    protected List<String> getDonationCategoryHeaders() {
        List<String> donationCategories = new ArrayList<String>();
        for (Donation donation : searchResults) {
            for (DonationCategory donationCategory : donation.getCategoryList()) {
                if (!donationCategories.contains(donationCategory.getCategoryName())) {
                    donationCategories.add(donationCategory.getCategoryName());
                }
            }
        }
        return donationCategories;
    }


    private List<Donation> recentDonations() {
        List<Donation> donationList = new ArrayList<Donation>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from Donation order by date desc");
        q.setMaxResults(UIConstants.MAX_RECENT_RESULTS);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            donationList = list;
        }
        return donationList;
    }

    /**
     * Performs the search for donations
     */
    private void performDonationSearch() {

    }

    private void setupResultsTable() {

        List<IColumn> columns = new ArrayList<IColumn>();

        columns.add(new PropertyColumn<String, String>(new Model<String>("Name"), "name", "name"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Member"), "memberID", "memberID"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Record No"), "receiptNo", "receiptNo"));
        columns.add(new PropertyColumn(new Model<String>("Total"), "total", "total"));
        columns.add(new ViewEditDelColumn(new Model<String>(""), null) {
            @Override
            public AbstractLink createViewLink(String id, IModel rowModel) {
                final Donation donation = (Donation) rowModel.getObject();
                AbstractLink viewLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    }
                };
                // Don't show a view link for now.
                //viewLink.setVisible(false);

                return viewLink;
            }

            @Override
            public AbstractLink createEditLink(String id, IModel rowModel) {
                final Donation donation = (Donation) rowModel.getObject();
                AbstractLink editLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        DonationNewPage donationNewPage = new DonationNewPage();
                        donationNewPage.setEditMode(donation);
                        getRequestCycle().setResponsePage(donationNewPage);
                    }
                };
                return editLink;
            }

            @Override
            public AbstractLink createDeleteLink(String id, IModel rowModel) {
                final Donation donation = (Donation) rowModel.getObject();
                AbstractLink delLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    }
                };
                return delLink;
            }
        });

        CollectionDataProvider<Donation> dataProvider = new CollectionDataProvider<Donation>(searchResults);

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
