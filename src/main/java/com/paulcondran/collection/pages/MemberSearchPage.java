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
import com.paulcondran.collection.model.data.Member;
import com.paulcondran.collection.model.ui.MemberSearch;
import java.text.DecimalFormat;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.time.Duration;
import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * @author Paul Condran
 */
@AuthorizeInstantiation("admin")
public class MemberSearchPage extends BasePage {

    private MemberSearch memberSearch = new MemberSearch();

    private List<Member> searchResults = null;
    
    private CollectionDataProvider<Member> dataProvider;

    private CSVResourceStream csvResourceStream;

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

                for (Member member : searchResults) {
                    List<Object> memberObjects = new ArrayList<Object>();
                    memberObjects.add(member.getOrganisation());
                    memberObjects.add(member.getFamilyName());
                    memberObjects.add(member.getMemberID());
                    memberObjects.add(member.getDirectDebitRef());
                    memberObjects.add(member.getName());
                    memberObjects.add(member.getAddress1());
                    memberObjects.add(member.getSuburb());
                    memberObjects.add(member.getPostcode());
                    memberObjects.add(member.getState());
                    memberObjects.add(member.getContactNo());
                    memberObjects.add(member.getMobileNo());
                    memberObjects.add(member.getEmailAddress());
                    memberObjects.add(member.getYearOfBirth());
                    memberObjects.add(member.getDateFrom());
                    memberObjects.add(member.getDateTo());
                    
                    objectsList.add(memberObjects);
                }
                return objectsList;
            }

            @Override
            public CellProcessor[] getCellProcessors() {
                List<CellProcessor> processorList = new ArrayList<CellProcessor>();
                // Org 1	Familyname	MemberId	DDRef	Name	Address1	
                // Suburb	Postcode	State	ContactNo	Mobile	Email	YearOfBirth
                processorList.add(new Optional());             // Org
                processorList.add(new Optional());            //Fam Name
                processorList.add(new NotNull());             // memberId
                processorList.add(new Optional());      // DDRef
                processorList.add(new Optional());            // Name
                processorList.add(new Optional());            // Address1
                processorList.add(new Optional());            // Suburb

                processorList.add(new Optional());             // postcode
                processorList.add(new Optional());            //state
                processorList.add(new Optional());             // contactno
                processorList.add(new Optional());      // mobile
                processorList.add(new Optional());            // email
                processorList.add(new Optional());            // yearofbirth
                processorList.add(new Optional());            // dateFrom
                processorList.add(new Optional());            // dateTo
                
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
        resourceStream.setFileName("MembersList.csv");
        resourceStream.setCacheDuration(Duration.NONE);

        form.add(new ResourceLink("csvExport", resourceStream));
    }

    /**
     * Gets a dynamic list of the headers for CSV export
     * @return
     */
    protected List<String> getCSVHeaders() {
        List<String> headers = new ArrayList<String>();
        headers.addAll(Arrays.asList("Organisation", "FamilyName", "MemberID", "DDRef", "Name", "Address1", "Suburb", "Postcode", "State", "contactNo", "MobileNo", "Email", "YearOfBirth", "DateFrom", "DateTo"));
        return headers;
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
        columns.add(new ViewEditDelColumn(new Model<String>(""), null) {
            @Override
            public AbstractLink createViewLink(String id, IModel rowModel) {
                final Member member = (Member) rowModel.getObject();
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
                final Member member = (Member) rowModel.getObject();
                AbstractLink editLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        MemberNewPage memberNewPage = new MemberNewPage();
                        memberNewPage.setEditMode(member);
                        getRequestCycle().setResponsePage(memberNewPage);
                    }
                };
                return editLink;
            }

            @Override
            public AbstractLink createDeleteLink(String id, IModel rowModel) {
                final Member member = (Member) rowModel.getObject();
                AbstractLink delLink = new AjaxSubmitLink(id) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                           CollectionDatabase.getInstance().remove(member);
//                        getRequestCycle().setResponsePage(this);
                           searchResults.remove(member);
                           
                           
                    }
                };
                return delLink;
            }
        });
        

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

        dataProvider = new CollectionDataProvider<Member>(searchResults);

        dataTable = new CollectionDataTable("searchResults", columns, dataProvider, UIConstants.MAX_RESULTS_PER_PAGE);
        dataTable.setOutputMarkupId(true);
        form.add(dataTable);

    }
}
