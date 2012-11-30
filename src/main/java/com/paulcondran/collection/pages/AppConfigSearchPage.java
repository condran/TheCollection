package com.paulcondran.collection.pages;

import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.CollectionDataProvider;
import com.paulcondran.collection.components.CollectionDataTable;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.components.ViewEditDelColumn;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.AppConfig;
import com.paulcondran.collection.model.data.User;
import com.paulcondran.collection.model.ui.AppConfigSearch;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Condran
 */
@AuthorizeInstantiation("Admin")
public class AppConfigSearchPage extends BasePage {

    private AppConfigSearch confSearch = new AppConfigSearch();

    private List<AppConfig> searchResults = null;

    private Form form;

    private DataTable dataTable;

    private CollectionDataProvider<AppConfig> dataProvider;


    public AppConfigSearchPage() {

        setupUserInterfaceFields();

        searchResults = listConfig();

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


        final DropDownChoice key1 = addDropdownField("key1",
                new PropertyModel<String>(confSearch, "key1"), CollectionUtil.listConfigKeys());


        // Hook into the search behaviour
        form.add(new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                CollectionDatabase db = CollectionDatabase.getInstance();
                CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<AppConfig> query = builder.createQuery(AppConfig.class);
                Root<AppConfig> confRoot = query.from(AppConfig.class);
                query.select(confRoot);

                List<Predicate> predicateList = confSearch.listPredicates(confRoot, builder);

                if (!predicateList.isEmpty()) {
                    Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
                    query.where(predicates);
                }

                searchResults = db.getEntityManager().createQuery(query).getResultList();
                dataProvider.setResults(searchResults);

                target.add(dataTable);
            }
        });

    }

    private List<AppConfig> listConfig() {
        List<AppConfig> userList = new ArrayList<AppConfig>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from AppConfig order by key1");
        q.setMaxResults(UIConstants.MAX_RECENT_RESULTS);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            userList = list;
        }
        for (Object o : list) {
            Hibernate.initialize(o);
        }
        return userList;
    }

    /**
     * Performs the search for donations
     */
    private void performUserSearch() {

    }

    private void setupResultsTable() {

        List<IColumn> columns = new ArrayList<IColumn>();

        columns.add(new PropertyColumn<String, String>(new Model<String>("Key1"), "key1", "key1"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Key2"), "key2", "key2"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Key3"), "key3", "key3"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Value"), "value", "value"));
        columns.add(new ViewEditDelColumn(new Model<String>(""), null) {
            @Override
            public AbstractLink createViewLink(String id, IModel rowModel) {
                final AppConfig conf = (AppConfig) rowModel.getObject();
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
                final AppConfig conf = (AppConfig) rowModel.getObject();
                AbstractLink editLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        AppConfigNewPage appConfigNewPage = new AppConfigNewPage();
                        appConfigNewPage.setEditMode(conf);
                        getRequestCycle().setResponsePage(appConfigNewPage);
                    }
                };
                return editLink;
            }

            @Override
            public AbstractLink createDeleteLink(String id, IModel rowModel) {
                final AppConfig conf = (AppConfig) rowModel.getObject();
                AbstractLink delLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                           CollectionDatabase.getInstance().remove(conf);
//                        getRequestCycle().setResponsePage(this);
                           searchResults.remove(conf);
                           
                           
                    }
                };
                return delLink;
            }
        });

        dataProvider = new CollectionDataProvider<AppConfig>(searchResults);

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
