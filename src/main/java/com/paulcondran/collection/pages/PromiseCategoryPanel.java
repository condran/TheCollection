package com.paulcondran.collection.pages;

import com.paulcondran.collection.functional.Function2Void;
import com.paulcondran.collection.model.data.DonationCategory;
import com.paulcondran.collection.model.data.PromiseCategory;
import com.paulcondran.collection.model.ui.OptionItem;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * This class encapsulates a single row of Category inputs
 */
public class PromiseCategoryPanel extends BasePanel {

    private PromiseCategory promiseCategory;

    // Constructor and component field setup
    public PromiseCategoryPanel(String id, PromiseCategory promiseCategory, List<OptionItem> listCategories,
                         final Function2Void<AjaxRequestTarget, PromiseCategory> promiseAdd) {
        super(id);

        this.promiseCategory = promiseCategory == null ? new PromiseCategory() : promiseCategory;

        TextField categoryAmount = addTextField("categoryAmount", new PropertyModel(getPromiseCategory(), "amount"));
        categoryAmount.add(new AjaxFormComponentUpdatingBehavior("onblur") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                promiseAdd.apply(target, getPromiseCategory());
            }
           
        });

        DropDownChoice category = addDropdownField("category", new PropertyModel(getPromiseCategory(), "categoryName"), listCategories);
        category.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // just update the model
            }
        });

    }

    public PromiseCategory getPromiseCategory() {
        return promiseCategory;
    }
}
