package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.CollectionDataTable;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.Member;
import com.paulcondran.collection.model.ui.MemberSearch;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Paul Condran
 */
@AuthorizeInstantiation("admin")
public class MemberSearchPage extends BasePage {

    private MemberSearch memberSearch = new MemberSearch();

    private List<Member> searchResults = null;

    private Form form;

    private DataTable dataTable;

    public MemberSearchPage() {

        setupUserInterfaceFields();

        searchResults = recentMemberChanges();
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


        addTextField("membersID", new PropertyModel<String>(memberSearch, "memberID"));
        addTextField("name", new PropertyModel<String>(memberSearch, "name"));
        addTextField("suburb", new PropertyModel<String>(memberSearch, "suburb"));
        addTextField("dateFrom", new PropertyModel<String>(memberSearch, "dateFrom"));
        addTextField("dateTo", new PropertyModel<String>(memberSearch, "dateTo"));

        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(memberSearch, "organisation"), CollectionUtil.listOrganisations());


        // Hook into the search behaviour
        form.add(new AjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                CollectionDatabase db = CollectionDatabase.getInstance();
                CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<Member> query = builder.createQuery(Member.class);
                Root<Member> donationRoot = query.from(Member.class);
                query.select(donationRoot);

                List<Predicate> predicateList = memberSearch.listPredicates(donationRoot, builder);

                if (!predicateList.isEmpty()) {
                    Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
                    query.where(predicates);
                }

                searchResults = db.getEntityManager().createQuery(query).getResultList();
                target.add(dataTable);


            }
        });

    }


    private List<Member> recentMemberChanges() {
        List<Member> memberList = new ArrayList<Member>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from Member order by dateFrom desc");
        q.setMaxResults(10);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            memberList = list;
        }

        return memberList;
    }

    /**
     * Performs the search for donations
     */
    private void performMemberSearch() {

    }

    private void setupResultsTable() {

        List<IColumn> columns = new ArrayList<IColumn>();

        columns.add(new PropertyColumn<String, String>(new Model<String>("Name"), "name", "name"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Member"), "memberID", "memberID"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Organisation"), "organisation", "organisation"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("StDate"), "dateFrom", "dateFrom"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("EndDate"), "dateTo", "dateTo"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("Suburb"), "suburb", "suburb"));
        columns.add(new PropertyColumn<String, String>(new Model<String>("State"), "state", "state"));

        SortableDataProvider dataProvider = new SortableDataProvider() {
            @Override
            public Iterator iterator(long l, long l1) {
                List newList = new ArrayList();
                for (long i = l; i < l+l1; i++) {
                    newList.add(searchResults.get((int)i));
                }
                return newList.iterator();
            }

            @Override
            public long size() {
                return searchResults.size();
            }

            @Override
            public IModel model(Object o) {
                return new Model((Member)o);
            }
        };

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, 5);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
