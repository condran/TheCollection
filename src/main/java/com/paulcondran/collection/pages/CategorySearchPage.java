package com.paulcondran.collection.pages;

import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.CollectionDataProvider;
import com.paulcondran.collection.components.CollectionDataTable;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.components.ViewEditDelColumn;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.CategoryDef;
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
public class CategorySearchPage extends BasePage {

    private List<CategoryDef> searchResults = null;

    private Form form;

    private DataTable dataTable;

    private CollectionDataProvider<CategoryDef> dataProvider;


    public CategorySearchPage() {
        setupUserInterfaceFields();

        searchResults = listCategories();

        setupResultsTable();

    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);
    }
    private List<CategoryDef> listCategories() {
        List<CategoryDef> userList = new ArrayList<CategoryDef>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from CategoryDef");
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

        columns.add(new PropertyColumn<String, String>(new Model<String>("CategoryID"), "categoryID", "categoryID"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Category Name"), "name", "name"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Promise?"), "promiseCategory", "promiseCategory"));
        columns.add(new ViewEditDelColumn(new Model<String>(""), null) {
            @Override
            public AbstractLink createViewLink(String id, IModel rowModel) {
                final CategoryDef def = (CategoryDef) rowModel.getObject();
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
                final CategoryDef def = (CategoryDef) rowModel.getObject();
                AbstractLink editLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        CategoryNewPage catNewPage = new CategoryNewPage();
                        catNewPage.setEditMode(def);
                        getRequestCycle().setResponsePage(catNewPage);
                    }
                };
                return editLink;
            }

            @Override
            public AbstractLink createDeleteLink(String id, IModel rowModel) {
                final CategoryDef def = (CategoryDef) rowModel.getObject();
                AbstractLink delLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                           CollectionDatabase.getInstance().remove(def);
//                        getRequestCycle().setResponsePage(this);
                           searchResults.remove(def);
                    }
                };
                return delLink;
            }
        });

        dataProvider = new CollectionDataProvider<CategoryDef>(searchResults);

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
