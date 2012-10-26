package com.tekinsure.thecollection.components;

import com.tekinsure.thecollection.model.ui.OptionItem;

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
        list.add(new OptionItem("ORG01", "American Express"));
        list.add(new OptionItem("ORG02", "McDonalds"));
        list.add(new OptionItem("ORG03", "Woolworths"));
        return list;
    }

    public static List<OptionItem> listOrganisations2() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        list.add(new OptionItem("ORG01", "Bakers Delight"));
        list.add(new OptionItem("ORG02", "Burger King"));
        list.add(new OptionItem("ORG03", "Coles"));
        return list;
    }



}
