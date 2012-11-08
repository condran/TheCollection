package com.paulcondran.collection.components;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 */
public class BootstrapToolbar extends NavigationToolbar {

    public BootstrapToolbar(DataTable<?, ?> table) {
        super(table);
    }

    @Override
    protected PagingNavigator newPagingNavigator(String navigatorId, DataTable<?, ?> table) {
        return new BootstrapPagingNavigator(navigatorId, table);
    }
}
