/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paulcondran.collection.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;

/**
 *
 * @author paul.condran
 */
public class StaticImage extends WebComponent {

    private String src;
    private String alt;


    public StaticImage(String id, String src)
    {
        super(id);
        this.src = src;
    }

    @Override
    protected void onComponentTag(ComponentTag tag)
    {
        super.onComponentTag(tag);
        checkComponentTag(tag, "img");
        tag.put("src", src);
    }

}
