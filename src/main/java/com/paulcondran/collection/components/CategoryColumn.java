package com.paulcondran.collection.components;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Displays a number of categories
 */
public abstract class CategoryColumn extends PropertyColumn {

    protected CategoryColumn(IModel<String> displayModel, Object sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item cellItem, String componentId, IModel rowModel) {
        StringBuilder output = new StringBuilder();
        output.append(processCategoriesSummary(rowModel));

        if (showLong(rowModel)) {
            output.append("<a href=\"#\" rel=\"popover\" data-placement=\"right\" data-html=\"true\" data-trigger=\"manual\" data-content=\"");
            output.append(processCategoriesLong(rowModel));
            output.append("\" data-original-title=\"Top 5 Categories\" onclick=\"$(this).popover('toggle');\">more</a>");
        }

        Label outputLabel = new Label(componentId, output.toString());
        outputLabel.setEscapeModelStrings(false);

        cellItem.add(outputLabel);
    }

    /**
     * A string of categories to show in the table
     * @param rowModel
     * @return
     */
    public abstract String processCategoriesSummary(IModel rowModel);

    /**
     * Return true to display the popover link
     * @param rowModel
     * @return
     */
    public abstract boolean showLong(IModel rowModel);

    /**
     * A string of categories to display in the pop-over (will be called if showLong returns true)
     * @param rowModel
     * @return
     */
    public abstract String processCategoriesLong(IModel rowModel);


}
