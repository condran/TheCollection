package com.paulcondran.collection.pages;

import com.paulcondran.collection.dataimports.ImportMembers;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * The new donation page controller. Handles creating and updating new donations.
 *
 * @author Paul Condran
 */
@AuthorizeInstantiation("admin")
public class ImportMembersPage extends BasePage {

    private FileUploadField fileUpload;
    
    private Form form;

    public ImportMembersPage() {

        setupUserInterfaceFields();

        Button uploadButton = new Button("upload") {
            @Override
            public void onSubmit() {
                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {

                        try {
                                ImportMembers di = new ImportMembers();
                                di.processWorksheet(uploadedFile.getInputStream());
                                info("saved file: " + uploadedFile.getClientFileName());
                        } catch (Exception e) {
                            e.printStackTrace();
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

}
