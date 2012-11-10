package com.paulcondran.collection.components;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 * A paging toolbar that outputs bootstrap friendly markup
 *
 */
public class BootstrapPagingNavigator extends PagingNavigator {

    public BootstrapPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }

    public BootstrapPagingNavigator(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
        super(id, pageable, labelProvider);
    }
}
