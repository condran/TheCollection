package com.paulcondran.collection.pages;

import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.dataimports.ImportDonations;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;


import org.apache.wicket.markup.html.form.DropDownChoice;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("admin")
public class ImportDonationsPage extends BasePage {

    private FileUploadField fileUpload;
    
    private String organisation;
    
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
                                di.organisation = getOrganisation();
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
    public String getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation the organisation to set
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

}
