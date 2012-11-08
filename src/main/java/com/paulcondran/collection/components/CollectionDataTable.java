package com.paulcondran.collection.components;

import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Custom datatable based on DefaultDataTable
 */
public class CollectionDataTable<T, S> extends DataTable<T, S> {

    /**
     * Constructor
     *
     * @param id
     *            component id
     * @param columns
     *            list of columns
     * @param dataProvider
     *            data provider
     * @param rowsPerPage
     *            number of rows per page
     */
    public CollectionDataTable(final String id, final List<? extends IColumn<T, S>> columns,
                            final ISortableDataProvider<T, S> dataProvider, final int rowsPerPage)
    {
        super(id, columns, dataProvider, rowsPerPage);

        addBottomToolbar(new BootstrapToolbar(this));
        addTopToolbar(new HeadersToolbar<S>(this, dataProvider));
        addBottomToolbar(new NoRecordsToolbar(this));
    }

    @Override
    protected Item<T> newRowItem(final String id, final int index, final IModel<T> model)
    {
        return new OddEvenItem<T>(id, index, model);
    }

}
