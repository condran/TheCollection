package com.paulcondran.collection.components;

import com.paulcondran.collection.model.ui.OptionItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 *
 */
public class ChoicePropertyModel implements IModel {

    private PropertyModel<String> model;
    private List<OptionItem> values;

    public ChoicePropertyModel(PropertyModel<String> model, List<OptionItem> values) {
        this.model = model;
        this.values = values;
    }

    @Override
    public Object getObject() {
        String code =  model.getObject();
        for (OptionItem item : values) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void setObject(Object o) {
        OptionItem item = (OptionItem)o;
        if (o != null) {
            model.setObject(item.getCode());
        }
        else {
            model.setObject(null);
        }
    }

    @Override
    public void detach() {

    }
}
