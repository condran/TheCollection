package com.tekinsure.thecollection.components;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * A column to encapsulate action links
 *
 */
public abstract class ViewEditDelColumn extends AbstractColumn {

    public ViewEditDelColumn(IModel<String> displayModel, Object sortProperty) {
        super(displayModel, sortProperty);
    }

    @Override
    public void populateItem(Item cellItem, String componentId, IModel rowModel) {
        AbstractLink viewLink = createViewLink("viewLink", rowModel);
        AbstractLink editLink = createEditLink("editLink", rowModel);
        AbstractLink delLink = createDeleteLink("delLink", rowModel);

        ViewEditDelColumnContainer container = new ViewEditDelColumnContainer(componentId);
        container.add(viewLink);
        container.add(editLink);
        container.add(delLink);

        cellItem.add(container);
    }

    @Override
    public String getCssClass() {
        return "span2";
    }

    public abstract AbstractLink createViewLink(String id, IModel rowModel);
    public abstract AbstractLink createEditLink(String id, IModel rowModel);
    public abstract AbstractLink createDeleteLink(String id, IModel rowModel);
}
