package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.functional.Function1Void;
import com.tekinsure.thecollection.functional.Function2Void;
import com.tekinsure.thecollection.model.data.DonationCategory;
import com.tekinsure.thecollection.model.ui.OptionItem;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class encapsulates a single row of Category inputs
 */
public class CategoryPanel extends BasePanel {

    private DonationCategory donationCategory;

    // Constructor and component field setup
    public CategoryPanel(String id, DonationCategory donationCategory, List<OptionItem> listCategories,
                         final Function2Void<AjaxRequestTarget, DonationCategory> donationAdd) {
        super(id);

        this.donationCategory = donationCategory == null ? new DonationCategory() : donationCategory;

        TextField categoryAmount = addTextField("categoryAmount", new PropertyModel(getDonationCategory(), "amount"));
        categoryAmount.add(new AjaxFormComponentUpdatingBehavior("onblur") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                donationAdd.apply(target, getDonationCategory());
            }
        });

        addDropdownField("category", new PropertyModel(getDonationCategory(), "categoryName"), listCategories);
    }

    public DonationCategory getDonationCategory() {
        return donationCategory;
    }
}
