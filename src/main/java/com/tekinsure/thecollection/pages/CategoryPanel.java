package com.tekinsure.thecollection.pages;

import com.tekinsure.thecollection.model.data.Category;
import com.tekinsure.thecollection.model.data.DonationCategory;
import com.tekinsure.thecollection.model.ui.OptionItem;
import fj.F;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 */
public class CategoryPanel extends BasePanel {

    private DonationCategory donationCategory;

    // Constructor and component field setup
    public CategoryPanel(String id, DonationCategory donationCategory, List<OptionItem> listCategories) {
        super(id);

        this.donationCategory = donationCategory == null ? new DonationCategory() : donationCategory;

        TextField categoryAmount = addTextField("categoryAmount", new PropertyModel(getDonationCategory(), "amount"));
        categoryAmount.add(new AjaxFormComponentUpdatingBehavior("oninput") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

            }
        });

        addDropdownField("category", new PropertyModel(getDonationCategory(), "categoryName"), listCategories);
    }

    public DonationCategory getDonationCategory() {
        return donationCategory;
    }
}
