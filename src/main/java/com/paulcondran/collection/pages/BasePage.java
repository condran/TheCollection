package com.paulcondran.collection.pages;

import com.paulcondran.collection.SessionBean;
import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.components.ChoicePropertyModel;
import com.paulcondran.collection.components.MenuPanel;
import com.paulcondran.collection.model.ui.OptionItem;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base Page
 * This class contains common logic for all pages in the application.
 *
 * @author Paul Condran
 */
public class BasePage extends WebPage {

    private MarkupContainer markupContainer = null;
    private Map<String, Component> fieldMap = new HashMap<String, Component>();
    private boolean addCommonComponents = true;

    public BasePage() {
        getSessionBean().setActivePageName(this.getClass().getSimpleName());
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // Add common page components, such as the menu and user links
        if (addCommonComponents) {
            // Navigation menu
            add(new MenuPanel("menu"));
            // Footer & Copyright
            Label footer = new Label("footer", UIConstants.FOOTER_TEXT);
            footer.setEscapeModelStrings(false);
            add(footer);
            // User Link
            AjaxLink userLink = new AjaxLink("userLink") {
                @Override
                public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                    // Do nothing for now
                }
            };
            userLink.add(new Label("userName", getSessionBean().getUserName()));
            add(userLink);
        }
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
        fieldMap.put(field.getId(), field);
    }

    public void updateComponent(AjaxRequestTarget target, String id) {
        updateComponent(target, Arrays.asList(id));
    }

    public Component getField(String id) {
        return fieldMap.get(id);
    }

    /**
     * Finds the component and adds it to the Ajax Target so the field can be updated
     * @param target   the AjaxRequestTarget object
     * @param ids      a list of field ids
     */
    public void updateComponent(AjaxRequestTarget target, List<String> ids) {
        for (String id : ids) {
            Component c = fieldMap.get(id);
            if (c != null) {
                target.add(c);
            }
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

    public TextArea addTextArea(String id, IModel model) {
        TextArea field = new TextArea(id, model);
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

    public boolean isAddCommonComponents() {
        return addCommonComponents;
    }

    public void setAddCommonComponents(boolean addCommonComponents) {
        this.addCommonComponents = addCommonComponents;
    }

    public SessionBean getSessionBean() {
        return (SessionBean)getSession();
    }
}