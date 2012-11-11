package com.paulcondran.collection.pages;

import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.CollectionDataProvider;
import com.paulcondran.collection.components.CollectionDataTable;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.components.ViewEditDelColumn;
import com.paulcondran.collection.data.CSVResourceStream;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.Promise;
import com.paulcondran.collection.model.data.PromiseCategory;
import com.paulcondran.collection.model.ui.OptionItem;
import com.paulcondran.collection.model.ui.PromiseSearch;
import java.math.BigDecimal;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

/**
 * @author Paul Condran
 */
@AuthorizeInstantiation("user")
public class PromiseSearchPage extends BasePage {

    private PromiseSearch promiseSearch = new PromiseSearch();

    private List<Promise> searchResults = null;

    private Form form;

    private DataTable dataTable;

    private CollectionDataProvider<Promise> dataProvider;

    private CSVResourceStream csvResourceStream;

    public PromiseSearchPage() {

        setupUserInterfaceFields();

        searchResults = recentPromises();

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


        addTextField("memberID", new PropertyModel<String>(promiseSearch, "memberID"));
        addTextField("name", new PropertyModel<String>(promiseSearch, "name"));
        addTextField("finYear", new PropertyModel<String>(promiseSearch, "finYear"));
        addTextField("ddt", new PropertyModel<String>(promiseSearch, "ddt"));

        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(promiseSearch, "organisation"), CollectionUtil.listEmptyList());


        // Hook into the search behaviour
        form.add(new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                CollectionDatabase db = CollectionDatabase.getInstance();
                CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<Promise> query = builder.createQuery(Promise.class);
                Root<Promise> promiseRoot = query.from(Promise.class);
                query.select(promiseRoot);

                List<Predicate> predicateList = promiseSearch.listPredicates(promiseRoot, builder);

                if (!predicateList.isEmpty()) {
                    Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
                    query.where(predicates);
                }

                searchResults = db.getEntityManager().createQuery(query).getResultList();
                dataProvider.setResults(searchResults);

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
                List<String> primiseCategoryHeaders = getPromiseCategoryHeaders();

                for (Promise promise : searchResults) {
                    List<Object> promiseObjects = new ArrayList<Object>();
                    promiseObjects.add(promise.getMemberID());
                    promiseObjects.add(promise.getName());
                    promiseObjects.add(promise.getOrganisation());
                    promiseObjects.add(promise.getFinancialYear());
                    promiseObjects.add(promise.getDirectDebitRef());
                    promiseObjects.add(promise.getTotal());
                    for (String categoryHeader : primiseCategoryHeaders) {
                        boolean itemAdded = false;
                        for (PromiseCategory promiseCategory : promise.getCategoryList()) {
                            if (categoryHeader.equals(promiseCategory.getCategoryName())) {
                                promiseObjects.add(promiseCategory.getAmount());
                                itemAdded = true;
                            }
                        }
                        if (!itemAdded) {
                            promiseObjects.add(BigDecimal.ZERO);
                        }
                    }

                    objectsList.add(promiseObjects);
                }
                return objectsList;
            }

            @Override
            public CellProcessor[] getCellProcessors() {
                List<CellProcessor> processorList = new ArrayList<CellProcessor>();
                processorList.add(new NotNull());             // memberID
                processorList.add(new Optional());            //name
                processorList.add(new NotNull());             // org
                processorList.add(new NotNull());      // finyear
                processorList.add(new Optional());            // directDebitRef
                processorList.add(new FmtNumber((DecimalFormat)DecimalFormat.getCurrencyInstance())); // total
                int headersLength = getPromiseCategoryHeaders().size();
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
        resourceStream.setFileName("Promises.csv");
        resourceStream.setCacheDuration(Duration.NONE);

        form.add(new ResourceLink("csvExport", resourceStream));
    }

    /**
     * Gets a dynamic list of the headers for CSV export
     * @return
     */
    protected List<String> getCSVHeaders() {
        List<String> headers = new ArrayList<String>();
        headers.addAll(Arrays.asList("memberID", "name", "organisation", "finYear","directDebitref", "total"));
        headers.addAll(getPromiseCategoryHeaders());
        return headers;
    }

    /**
     * Gets a list of unique categories across the search results
     *
     * @return
     */
    protected List<String> getPromiseCategoryHeaders() {
        List<String> promiseCategories = new ArrayList<String>();
        for (OptionItem promiseCategory : CollectionUtil.listPromiseCategories()) {
                promiseCategories.add(promiseCategory.getDescription());
        }
        return promiseCategories;
    }


    private List<Promise> recentPromises() {
        List<Promise> promiseList = new ArrayList<Promise>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from Promise order by financialYear desc");
        q.setMaxResults(UIConstants.MAX_RECENT_RESULTS);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            promiseList = list;
        }
        for (Object o : list) {
            Hibernate.initialize(o);
        }
        return promiseList;
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
        columns.add(new PropertyColumn<String, String>(new Model<String>("FinancialYear"), "financialYear", "financialYear"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Organisation"), "organisation", "organisation"));
        columns.add(new PropertyColumn(new Model<String>("Total"), "total", "total"));
        columns.add(new ViewEditDelColumn(new Model<String>(""), null) {
            @Override
            public AbstractLink createViewLink(String id, IModel rowModel) {
                final Promise promise = (Promise) rowModel.getObject();
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
                final Promise promise = (Promise) rowModel.getObject();
                AbstractLink editLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        PromiseNewPage promiseNewPage = new PromiseNewPage();
                        promiseNewPage.setEditMode(promise);
                        getRequestCycle().setResponsePage(promiseNewPage);
                    }
                };
                return editLink;
            }

            @Override
            public AbstractLink createDeleteLink(String id, IModel rowModel) {
                final Promise promise = (Promise) rowModel.getObject();
                AbstractLink delLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                           CollectionDatabase.getInstance().remove(promise);
//                        getRequestCycle().setResponsePage(this);
                           searchResults.remove(promise);
                    }
                };
                return delLink;
            }
        });

        dataProvider = new CollectionDataProvider<Promise>(searchResults);

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
