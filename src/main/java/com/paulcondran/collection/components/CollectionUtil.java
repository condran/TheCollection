package com.paulcondran.collection.components;


import com.paulcondran.collection.model.ui.OptionItem;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
        list.add(new OptionItem("C001", "Flood fund"));
        list.add(new OptionItem("C002", "Orphans fund"));
        list.add(new OptionItem("C003", "Family fund"));
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
        list.add(new OptionItem("ORG 1", "American Express"));
        list.add(new OptionItem("ORG 2", "McDonalds"));
        list.add(new OptionItem("ORG 3", "Woolworths"));
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
}
