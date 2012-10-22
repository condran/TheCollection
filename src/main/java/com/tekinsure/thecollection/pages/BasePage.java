package com.tekinsure.thecollection.pages;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
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

    private MarkupContainer markupContainer = null;

    public BasePage() {

    }

    public BasePage(PageParameters parameters) {
        super(parameters);
    }

    public MarkupContainer getMarkupContainer() {
        return markupContainer;
    }

    public void setMarkupContainer(MarkupContainer markupContainer) {
        this.markupContainer = markupContainer;
    }

    /**
     * Convenience method to add a text field to the page. If you need to set a different parent
     * set {@link setMarkupContainer(MarkupContainer) setMarkupContainer}
     *
     * @param id       The component id
     * @param model    The IModel binding for the component
     * @return
     */
    public Component addTextField(String id, IModel model) {
        TextField field = new TextField(id, model);
        if (markupContainer != null) {
            markupContainer.add(field);
        } else {
            add(field);
        }
        return field;
    }
}
