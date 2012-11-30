package com.paulcondran.collection.components;


import com.paulcondran.collection.UIConstants;
import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.AppConfig;
import com.paulcondran.collection.model.data.CategoryDef;
import com.paulcondran.collection.model.data.User;
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

        Query q = em.createQuery("from CategoryDef where promiseCategory='true'");

        List<CategoryDef> catList = q.getResultList();
        for (CategoryDef def : catList)
        {
            list.add(new OptionItem(def.getCategoryID(), def.getName()));
        }
        return list;
    }
    public static User loadUser(String username) {
        
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        StringBuilder sb = new StringBuilder();
        sb.append("from User where userID='"+username+"'");
        
        Query q = em.createQuery(sb.toString());

        List<User> catList = q.getResultList();
        if (catList.size() >0) {
            return catList.get(0);
        } else
        return null;
    }

    public static List<AppConfig> loadConfigData(String key1, String key2, String key3) {
        
        CollectionDatabase db = CollectionDatabase.getInstance();
        EntityManager em = db.getEntityManager();

        StringBuilder sb = new StringBuilder();
        sb.append("from AppConfig where key1='"+key1+"'");
        if (!StringUtils.isBlank(key2)) {
            sb.append (" and key2='"+key2+"'");
        }
                
        if (!StringUtils.isBlank(key3)) {
            sb.append (" and key3='"+key3+"'");
        }
        
        Query q = em.createQuery(sb.toString());

        List<AppConfig> catList = q.getResultList();
        return catList;
    }
    public static List<OptionItem> listCollectors(String organisation) {
        List<OptionItem> list = new ArrayList<OptionItem>();
        List<AppConfig> cList = loadConfigData("Collector", organisation, null);
        for ( AppConfig conf : cList) {
            list.add(new OptionItem(conf.getValue(), conf.getValue()));
        }
        return list;
    }

    public static List<OptionItem> listOrganisations() {
        List<OptionItem> list = new ArrayList<OptionItem>();

        List<AppConfig> cList = loadConfigData("Organisation", null, null);
        for ( AppConfig conf : cList) {
            list.add(new OptionItem(conf.getValue(), conf.getValue()));
        }
        
        return list;
    }

    public static List<OptionItem> listConfigKeys() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        list.add(new OptionItem("Organisation", "Organisation"));
        list.add(new OptionItem("Collector", "Collector"));
        return list;
    }


    public static List<OptionItem> listUserTypes() {
        List<OptionItem> list = new ArrayList<OptionItem>();
        list.add(new OptionItem(UIConstants.ROLE_USER, UIConstants.ROLE_USER));
        list.add(new OptionItem(UIConstants.ROLE_COLLECTOR, UIConstants.ROLE_COLLECTOR));
        list.add(new OptionItem(UIConstants.ROLE_FINANCE, UIConstants.ROLE_FINANCE));
        list.add(new OptionItem(UIConstants.ROLE_CENTRAL_FINANCE, UIConstants.ROLE_CENTRAL_FINANCE));
        list.add(new OptionItem(UIConstants.ROLE_ADMIN, UIConstants.ROLE_ADMIN));
        return list;
    }
    
    public static <E extends Enum<E>> List<OptionItem> convertToOptionList(List<E> aList) {
        List<OptionItem> oList = new ArrayList<OptionItem>();
        for (Enum optValue : aList) {
            oList.add(new OptionItem(optValue.toString(), optValue.toString()));            
        }
            
         return oList;   
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
