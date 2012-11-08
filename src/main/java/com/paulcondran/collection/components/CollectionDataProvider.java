package com.paulcondran.collection.components;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple data provider
 */
public class CollectionDataProvider<T extends Serializable> extends SortableDataProvider {

    private List<T> results;

    public CollectionDataProvider(List<T> results) {
        this.results = results;
    }

    /**
     * Gets an iterator for the subset of total data
     *
     * @param first first row of data
     * @param count minimum number of elements to retrieve
     * @return iterator capable of iterating over {first, first+count} items
     */
    @Override
    public Iterator iterator(long first, long count) {
        List newList = new ArrayList();
        for (long i = first; i < first+count; i++) {
            newList.add(results.get((int)i));
        }
        return newList.iterator();
    }

    /**
     * Gets total number of items in the collection represented by the DataProvider
     *
     * @return total item count
     */
    @Override
    public long size() {
        return results.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Callback used by the consumer of this data provider to wrap objects retrieved from
     * {@link #iterator(long, long)} with a model (usually a detachable one).
     *
     * @param object the object that needs to be wrapped
     * @return the model representation of the object
     */
    @Override
    public IModel model(Object object) {
        return new Model((T) object);
    }
}
