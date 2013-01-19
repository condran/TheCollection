package com.paulcondran.collection.pages;

import com.paulcondran.collection.DonationStatus;
import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.*;
import com.paulcondran.collection.data.CSVResourceStream;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.DonationCategory;
import com.paulcondran.collection.model.data.MonthlyDeposit;
import com.paulcondran.collection.model.ui.DepositeSearch;
import java.math.BigDecimal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Button;
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
import org.hibernate.Hibernate;
import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.EnumUtils;

/**
 * @author Paul Condran
 */
@AuthorizeInstantiation("User")
public class DepositeSearchPage extends BasePage {

    private DepositeSearch depositeSearch = new DepositeSearch();

    private List<MonthlyDeposit> searchResults = null;

    private Form form;

    private DataTable dataTable;

    private CollectionDataProvider<MonthlyDeposit> dataProvider;

    private CSVResourceStream csvResourceStream;
    private MonthlyDeposit deletionCandidate = null;
    DropDownChoice organisation;
    DropDownChoice collector;

    public DepositeSearchPage() {

        setupUserInterfaceFields();

        searchResults = recentDeposits();

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


        addTextField("month", new PropertyModel<String>(depositeSearch, "month"));
        addCheckboxField("unclosed", new PropertyModel<Boolean>(depositeSearch, "unclosed"));
        addDropdownField("depositStatus", new PropertyModel(depositeSearch, "depositStatus"), CollectionUtil.listDepositStatus());

        organisation = addDropdownField("organisation",
                new PropertyModel<String>(depositeSearch, "organisation"), CollectionUtil.listOrganisations());


        collector = addDropdownField("collector", new PropertyModel<String>(depositeSearch, "collector"),
                CollectionUtil.listEmptyList());
        // Logic to determine list based on collector
        organisation.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                if (depositeSearch.getOrganisation() != null) {
                    collector.setChoices(CollectionUtil.listCollectors(depositeSearch.getOrganisation()));
                } else {
                    collector.setChoices(CollectionUtil.listEmptyList());
                }
            }
        });
        // Hook into the search behaviour
        form.add(new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                CollectionDatabase db = CollectionDatabase.getInstance();
                CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<MonthlyDeposit> query = builder.createQuery(MonthlyDeposit.class);
                Root<MonthlyDeposit> donationRoot = query.from(MonthlyDeposit.class);
                query.select(donationRoot);

                List<Predicate> predicateList = depositeSearch.listPredicates(donationRoot, builder);

                if (!predicateList.isEmpty()) {
                    Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
                    query.where(predicates);
                }

                searchResults = db.getEntityManager().createQuery(query).getResultList();
                dataProvider.setResults(searchResults);

                target.add(dataTable);


            }
        });

        Button deleteRecord = new AjaxButton("deleteConfirm") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (deletionCandidate != null) {
                    CollectionDatabase.getInstance().remove(deletionCandidate);
                    searchResults.remove(deletionCandidate);
                    deletionCandidate = null;

                    target.add(dataTable);
                }
                target.appendJavaScript("$('#modalDelete').modal('hide')");
            }
        };
        form.add(deleteRecord);


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

                for (MonthlyDeposit mDeposit : searchResults) {
                    List<Object> donationObjects = new ArrayList<Object>();
                    donationObjects.add(mDeposit.getOrganisation());
                    donationObjects.add(mDeposit.getCollectorId());
                    donationObjects.add(mDeposit.getDepositPeriod());
                    donationObjects.add(mDeposit.getDonationStatus().toString());
                    donationObjects.add(mDeposit.getTotal());

                    objectsList.add(donationObjects);
                }
                return objectsList;
            }

            @Override
            public CellProcessor[] getCellProcessors() {
                List<CellProcessor> processorList = new ArrayList<CellProcessor>();
                processorList.add(new NotNull());             // memberID
                processorList.add(new NotNull());             // recieptNo
                processorList.add(new NotNull());             // memberID
                processorList.add(new NotNull());             // recieptNo
                processorList.add(new NotNull());             // memberID

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
        resourceStream.setFileName("Deposits.csv");
        resourceStream.setCacheDuration(Duration.NONE);

        form.add(new ResourceLink("csvExport", resourceStream));
    }

    /**
     * Gets a dynamic list of the headers for CSV export
     * @return
     */
    protected List<String> getCSVHeaders() {
        List<String> headers = new ArrayList<String>();
        headers.addAll(Arrays.asList("org", "collector", "period", "status", "total"));
        return headers;
    }


    private List<MonthlyDeposit> recentDeposits() {
        List<MonthlyDeposit> donationList = new ArrayList<MonthlyDeposit>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from MonthlyDeposit order by depositPeriod desc");
        q.setMaxResults(UIConstants.MAX_RECENT_RESULTS);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            donationList = list;
        }
        for (Object o : list) {
            Hibernate.initialize(o);
        }
        return donationList;
    }

    private void setupResultsTable() {

        List<IColumn> columns = new ArrayList<IColumn>();

        columns.add(new PropertyColumn<String, String>(new Model<String>("Organisation"), "organisation", "organisation"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Collector"), "collectorId", "collectorId"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Period"), "depositPeriod", "depositPeriod"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Status"), "donationStatus", "donationStatus"));
        columns.add(new PropertyColumn(new Model<String>("Total"), "total", "total"));
        columns.add(new ViewEditDelColumn(new Model<String>(""), null) {
            @Override
            public AbstractLink createViewLink(String id, IModel rowModel) {
                final MonthlyDeposit mDep = (MonthlyDeposit) rowModel.getObject();
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
                final MonthlyDeposit mDep = (MonthlyDeposit) rowModel.getObject();
                AbstractLink editLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        DepositeNewPage depNewPage = new DepositeNewPage();
                        depNewPage.setEditMode(mDep);
                        getRequestCycle().setResponsePage(depNewPage);
                    }
                };
                if (mDep.getDonationStatus() != DonationStatus.Initial) {
                    editLink.setVisible(false);
                }
                return editLink;
            }

            @Override
            public AbstractLink createDeleteLink(String id, IModel rowModel) {
                final MonthlyDeposit mDep = (MonthlyDeposit) rowModel.getObject();
                AbstractLink delLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        deletionCandidate = mDep;
                        target.appendJavaScript("$('#modalDelete').modal('show')");
                    }
                };
                if (mDep.getDonationStatus() != DonationStatus.Initial) {
                    delLink.setVisible(false);
                }
                return delLink;
            }
        });

        dataProvider = new CollectionDataProvider<MonthlyDeposit>(searchResults);

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
