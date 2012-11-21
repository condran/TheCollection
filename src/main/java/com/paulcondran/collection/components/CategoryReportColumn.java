package com.paulcondran.collection.components;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * A specialised column to display category amounts
 */
public class CategoryReportColumn extends PropertyColumn {

    private String columnCode;
    private String categoryCodeExpression;
    private String categoryAmountExpression;
    private String categoryListExpression;

    private boolean lastRowBold = true;


    public CategoryReportColumn(IModel<String> displayModel, Object sortProperty, String propertyExpression,
                                String categoryListExpression, String categoryCodeExpression, String categoryAmountExpression) {
        super(displayModel, sortProperty, propertyExpression);
        this.columnCode = propertyExpression;
        this.categoryListExpression = categoryListExpression;
        this.categoryCodeExpression = categoryCodeExpression;
        this.categoryAmountExpression = categoryAmountExpression;
    }

    @Override
    public void populateItem(Item cellItem, String componentId, IModel rowModel) {

        List categoryList = (List)(new PropertyModel(rowModel, categoryListExpression)).getObject();
        BigDecimal amount = null;
        for (Object item : categoryList) {
            String code = (String)(new PropertyModel(item, categoryCodeExpression)).getObject();
            if (columnCode.equals(code)) {
                amount = (BigDecimal)(new PropertyModel(item, categoryAmountExpression)).getObject();
            }
        }

        String amountFormat = "";
        if (amount != null) {
            amountFormat = DecimalFormat.getCurrencyInstance().format(amount);
        }

        cellItem.add(new Label(componentId, new Model<String>(amountFormat)));

    }

    public boolean isLastRowBold() {
        return lastRowBold;
    }

    public void setLastRowBold(boolean lastRowBold) {
        this.lastRowBold = lastRowBold;
    }

}
