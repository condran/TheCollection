package com.tekinsure.thecollection.components;

import com.tekinsure.thecollection.functional.Function1Void;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Navigation Item
 */
public class NavItem extends Panel {


    public NavItem(String id, String label, boolean active, final Function1Void<AjaxRequestTarget> onclick) {
        super(id);

        if (active) {
            add(new AttributeModifier("class", "active"));
        }
        AjaxLink link = new AjaxLink("navLink") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                onclick.apply(ajaxRequestTarget);
            }
        };
        link.add(new Label("navLabel", label));
        add(link);

    }
}
