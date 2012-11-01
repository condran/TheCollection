package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.components.CollectionUtil;
import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.dataimports.ImportDonations;
import com.tekinsure.thecollection.model.ui.MemberNew;
import com.tekinsure.thecollection.model.ui.OptionItem;
import java.util.Date;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.wicket.markup.html.form.DropDownChoice;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
public class ImportDonationsPage extends BasePage {

    private FileUploadField fileUpload;
    
    private OptionItem organisation;
    
    private Form form;

    public ImportDonationsPage() {

        setupUserInterfaceFields();

        final DropDownChoice organisation = addDropdownField("organisation",
                new PropertyModel<String>(this, "organisation"), CollectionUtil.listOrganisations());


        Button uploadButton = new Button("upload") {
            @Override
            public void onSubmit() {
                if (getOrganisation() == null) {
                    warn("No Organisation selected");
                    return;
                }
                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {

                        try {
                                ImportDonations di = new ImportDonations();
                                di.organisation = getOrganisation().getCode();
                                di.processWorksheet(uploadedFile.getInputStream());
                                info("saved file: " + uploadedFile.getClientFileName());
                        } catch (Exception e) {
                                throw new IllegalStateException("Error");
                        }
                }

            }
        };
        form.setMultiPart(true);
        form.add(uploadButton);
        form.add(fileUpload = new FileUploadField("fileUpload"));
    }

    private void setupUserInterfaceFields() {

        form = new Form("form");
        form.setOutputMarkupId(true);
        add(form);
        setMarkupContainer(form);

        add(new FeedbackPanel("feedback"));

    }

    /**
     * Queries the Member database to fill out the member details
     */
    private void performMemberQuery() {

    }

    /**
     * @return the organisation
     */
    public OptionItem getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation the organisation to set
     */
    public void setOrganisation(OptionItem organisation) {
        this.organisation = organisation;
    }

}
