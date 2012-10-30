package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.components.ChoicePropertyModel;
import com.tekinsure.thecollection.model.ui.OptionItem;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

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

    private void addField(Component field) {
        field.setOutputMarkupId(true);
        if (markupContainer != null) {
            markupContainer.add(field);
        } else {
            add(field);
        }
    }

    /**
     * Convenience method to add a text field to the page. If you need to set a different parent
     * set {@link setMarkupContainer(MarkupContainer) setMarkupContainer}
     *
     * @param id       The component id
     * @param model    The IModel binding for the component
     * @return
     */
    public TextField addTextField(String id, IModel model) {
        TextField field = new TextField(id, model);
        addField(field);
        return field;
    }

    /**
     * Convenience mehtod to add a dropdown field to the page. If you need to set a different parent
     * set {@link setMarkupContainer(MarkupContainer) setMarkupContainer}
     *
     * @param id           The component id
     * @param model        The IModel binding for the component
     * @param optionList   The list of options to display
     * @return
     */
    public DropDownChoice addDropdownField(String id, final PropertyModel model, List<OptionItem> optionList) {
        ChoiceRenderer choiceRenderer = new ChoiceRenderer("description", "code");
        DropDownChoice field = new DropDownChoice(id, new ChoicePropertyModel(model, optionList), optionList, choiceRenderer);
        addField(field);
        return field;
    }
}
