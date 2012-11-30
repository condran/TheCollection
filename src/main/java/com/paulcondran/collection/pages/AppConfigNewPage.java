package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.BootstrapFeedbackPanel;
import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.AppConfig;
import com.paulcondran.collection.model.ui.AppConfigNew;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("Admin")
public class AppConfigNewPage extends BasePage {

    private AppConfigNew confNew = new AppConfigNew();
    private Form form;
    private FeedbackPanel feedbackPanel;

    public AppConfigNewPage() {

        setupUserInterfaceFields();
        
        final DropDownChoice ddkey1 = addDropdownField("key1",
                new PropertyModel<String>(confNew, "key1"), CollectionUtil.listConfigKeys());

        Button saveButton = new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(validateForm(target)) {
                    confNew.getAppConfig().setKey1(confNew.getKey1());
                    CollectionDatabase db =  CollectionDatabase.getInstance();
                    db.persist(confNew.getAppConfig());
                    getRequestCycle().setResponsePage(AppConfigSearchPage.class);
                }
            }
        };
        form.add(saveButton);
    }
    public void setEditMode(AppConfig conf) {
        this.confNew.setAppConfig(conf);
        confNew.setKey1( conf.getKey1());
    }
    private boolean validateForm(AjaxRequestTarget target) {
        boolean valid = true;
        if (StringUtils.isBlank(confNew.getKey1())) {
            error("Key1 is required");
            valid = false;
        }
        if (StringUtils.isBlank(confNew.getAppConfig().getValue())) {
            error("Value is required");
            valid = false;
        }
        if (locateConfig()) {
            error("This config entry already exists");
            valid = false;
        }
        target.add(feedbackPanel);
        return valid;
    }
private boolean locateConfig() {
            List<AppConfig> confList = new ArrayList<AppConfig>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from AppConfig where key1='"+confNew.getKey1()+"' and key2='"+confNew.getAppConfig().getKey2()+"' and key3='"+confNew.getAppConfig().getKey3()+"' and value='"+confNew.getAppConfig().getValue()+"'");
        q.setMaxResults(1);

        List list = q.getResultList();
        if (!list.isEmpty()) {
            return true;
        }
        else return false;
}
    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);


        addTextField("key2", new PropertyModel<String>(confNew, "appConfig.key2"));
        addTextField("key3", new PropertyModel<String>(confNew, "appConfig.key3"));
        addTextField("value", new PropertyModel<String>(confNew, "appConfig.value"));

        add(feedbackPanel = new BootstrapFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

    }

}
