package com.paulcondran.collection.pages;

import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.CollectionDataProvider;
import com.paulcondran.collection.components.CollectionDataTable;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.components.ViewEditDelColumn;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.User;
import com.paulcondran.collection.model.ui.UserSearch;
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
@AuthorizeInstantiation("admin")
public class UserSearchPage extends BasePage {

    private UserSearch userSearch = new UserSearch();

    private List<User> searchResults = null;

    private Form form;

    private DataTable dataTable;

    private CollectionDataProvider<User> dataProvider;


    public UserSearchPage() {

        setupUserInterfaceFields();

        searchResults = listUsers();

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


        addTextField("userID", new PropertyModel<String>(userSearch, "userID"));
        addTextField("name", new PropertyModel<String>(userSearch, "name"));

        final DropDownChoice userType = addDropdownField("type",
                new PropertyModel<String>(userSearch, "type"), CollectionUtil.listUserTypes());


        // Hook into the search behaviour
        form.add(new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                CollectionDatabase db = CollectionDatabase.getInstance();
                CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<User> query = builder.createQuery(User.class);
                Root<User> userRoot = query.from(User.class);
                query.select(userRoot);

                List<Predicate> predicateList = userSearch.listPredicates(userRoot, builder);

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

    private List<User> listUsers() {
        List<User> userList = new ArrayList<User>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from User order by type");
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

        columns.add(new PropertyColumn<String, String>(new Model<String>("Name"), "name", "name"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("UserID"), "userID", "userID"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Role"), "type", "type"));
        columns.add(new ViewEditDelColumn(new Model<String>(""), null) {
            @Override
            public AbstractLink createViewLink(String id, IModel rowModel) {
                final User user = (User) rowModel.getObject();
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
                final User user = (User) rowModel.getObject();
                AbstractLink editLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        UserNewPage userNewPage = new UserNewPage();
                        userNewPage.setEditMode(user);
                        getRequestCycle().setResponsePage(userNewPage);
                    }
                };
                return editLink;
            }

            @Override
            public AbstractLink createDeleteLink(String id, IModel rowModel) {
                final User user = (User) rowModel.getObject();
                AbstractLink delLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                           CollectionDatabase.getInstance().remove(user);
//                        getRequestCycle().setResponsePage(this);
                           searchResults.remove(user);
                           
                           
                    }
                };
                return delLink;
            }
        });

        dataProvider = new CollectionDataProvider<User>(searchResults);

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
