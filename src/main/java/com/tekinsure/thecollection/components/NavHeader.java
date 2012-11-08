package com.tekinsure.thecollection.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;


/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 8/11/12
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NavHeader extends Panel {

    public NavHeader(String id, String label) {
        super(id);

        add(new Label("navHeader", label));
        add(new AttributeModifier("class", "nav-header"));
    }
}
