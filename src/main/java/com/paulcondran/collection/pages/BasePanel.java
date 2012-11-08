package com.paulcondran.collection.pages;

import com.paulcondran.collection.SessionBean;
import com.paulcondran.collection.components.ChoicePropertyModel;
import com.paulcondran.collection.model.ui.OptionItem;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * Base Page
 * This class contains common logic for all pages in the application.
 *
 * @author Paul Condran
 */
public class BasePanel extends Panel {

    private MarkupContainer markupContainer = null;

    /**
     * @see org.apache.wicket.Component#Component(String)
     */
    public BasePanel(String id) {
        super(id);
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
     * set {@link setMarkupContainer( org.apache.wicket.MarkupContainer) setMarkupContainer}
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
     * set {@link setMarkupContainer( org.apache.wicket.MarkupContainer) setMarkupContainer}
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

    public SessionBean getSessionBean() {
        return (SessionBean)getSession();
    }
}
