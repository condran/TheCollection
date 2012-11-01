package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.components.CollectionUtil;
import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.dataimports.ImportMembers;
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
