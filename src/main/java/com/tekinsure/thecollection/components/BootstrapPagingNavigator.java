package com.tekinsure.thecollection.components;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;

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
