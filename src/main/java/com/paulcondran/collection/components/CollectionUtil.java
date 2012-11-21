package com.paulcondran.collection.components;


import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.CategoryDef;
import com.paulcondran.collection.model.ui.OptionItem;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Just some useful utility methods
 */
public class CollectionUtil {

    public static List<OptionItem> listEmptyList() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        return list;
    }

    public static List<OptionItem> listCategories() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from CategoryDef");
        q.setMaxResults(UIConstants.MAX_RECENT_RESULTS);

        List<CategoryDef> catList = q.getResultList();
        for (CategoryDef def : catList)
        {
            list.add(new OptionItem(def.getCategoryID(), def.getName()));
        }
        return list;
    }

    public static List<OptionItem> listPromiseCategories() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        Query q = em.createQuery("from CategoryDef where isPromise='true'");
        q.setMaxResults(UIConstants.MAX_RECENT_RESULTS);

        List<CategoryDef> catList = q.getResultList();
        for (CategoryDef def : catList)
        {
            list.add(new OptionItem(def.getCategoryID(), def.getName()));
        }
        return list;
    }

    public static List<OptionItem> listCollectors() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        list.add(new OptionItem("CL01", "Bart Simpson"));
        list.add(new OptionItem("CL02", "Fred Flintsone"));
        list.add(new OptionItem("CL03", "Eric Cartman"));
        return list;
    }

    public static List<OptionItem> listOrganisations() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        list.add(new OptionItem("Alsadaqat", "Alsadaqat"));
        return list;
    }

    public static List<OptionItem> listOrganisations2() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        list.add(new OptionItem("ORG01", "Bakers Delight"));
        list.add(new OptionItem("ORG02", "Burger King"));
        list.add(new OptionItem("ORG03", "Coles"));
        return list;
    }

    public static List<OptionItem> listUserTypes() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        list.add(new OptionItem("User", "User"));
        list.add(new OptionItem("Collector", "Collector"));
        list.add(new OptionItem("Finance", "Finance"));
        list.add(new OptionItem("Admin", "Admin"));
        return list;
    }

    /**
     * Calls appendIfNotBlank with the formatter of "%s "
     */
    public static StringBuilder appendIfNoBlank(StringBuilder builder, String value) {
        return appendIfNotBlank(builder, value, "%s ");
    }

    /**
     * If the value is not null or blank, appends to the string builder using a format
     * @param builder  a StringBuilder object
     * @param value    the value to append
     * @param format   see String.format
     * @return
     */
    public static StringBuilder appendIfNotBlank(StringBuilder builder, String value, String format) {
        if (StringUtils.isNotBlank(value)) {
            if (StringUtils.isNotBlank(format)) {
               builder.append(String.format(format,value));
            }
            else {
                builder.append(value);
            }
        }
        return builder;
    }

    public static boolean isOptionCodeInList(String code, List<OptionItem> list) {
        for (OptionItem optionItem : list) {
            if (optionItem.getCode().equals(code))
                return true;
        }
        return false;
    }

    public static OptionItem findOption(String code, List<OptionItem> options) {
        for (OptionItem optionItem : options) {
            if (optionItem.getCode().equals(code)) {
                return optionItem;
            }
        }
        return new OptionItem();
    }
}
