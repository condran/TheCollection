package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.AppConfig;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 21/10/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppConfigSearch {

    private String key1;
    private String key2;
    private String key3;

    
    public List<Predicate> listPredicates(Root<AppConfig> donationRoot, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(getKey1())) {
            Predicate userID = builder.like(
                    builder.lower(donationRoot.<String>get("key1")), getKey1());
            predicateList.add(userID);
        }

        if (StringUtils.isNotBlank(getKey2())) {
            Predicate userID = builder.like(
                    builder.lower(donationRoot.<String>get("key2")), getKey2());
            predicateList.add(userID);
        }

        if (StringUtils.isNotBlank(getKey3())) {
            Predicate userID = builder.like(
                    builder.lower(donationRoot.<String>get("key3")), getKey3());
            predicateList.add(userID);
        }

        return predicateList;
    }

    /**
     * @return the key1
     */
    public String getKey1() {
        return key1;
    }

    /**
     * @param key1 the key1 to set
     */
    public void setKey1(String key1) {
        this.key1 = key1;
    }

    /**
     * @return the key2
     */
    public String getKey2() {
        return key2;
    }

    /**
     * @param key2 the key2 to set
     */
    public void setKey2(String key2) {
        this.key2 = key2;
    }

    /**
     * @return the key3
     */
    public String getKey3() {
        return key3;
    }

    /**
     * @param key3 the key3 to set
     */
    public void setKey3(String key3) {
        this.key3 = key3;
    }

}
