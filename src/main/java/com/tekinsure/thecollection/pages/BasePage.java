package com.tekinsure.thecollection.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base Page
 * This class contains common logic for all pages in the application.
 *
 * @author Paul Condran
 */
public class BasePage extends WebPage {

    public BasePage() {

    }

    public BasePage(PageParameters parameters) {
        super(parameters);
    }


    public Component addTextField(String id, IModel model) {
        TextField field = new TextField(id, model);
        add(field);
        return field;
    }
}
