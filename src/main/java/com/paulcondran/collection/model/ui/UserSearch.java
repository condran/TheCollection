package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.User;
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
public class UserSearch {

    private String userID;
    private String name;
    private String type;

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserID() {
        return userID;
    }

    public void setUserID(String memberID) {
        this.userID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public List<Predicate> listPredicates(Root<User> donationRoot, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(getUserID())) {
            Predicate userID = builder.like(
                    builder.lower(donationRoot.<String>get("userID")), "%" + getUserID().toLowerCase() + "%");
            predicateList.add(userID);
        }

        if (StringUtils.isNotBlank(getName())) {
            Predicate name = builder.like(builder.lower(donationRoot.<String>get("name")), "%" + getName().toLowerCase() + "%");
            predicateList.add(name);
        }

        if (StringUtils.isNotBlank(getType())) {
            Predicate type = builder.like(
                    builder.lower(donationRoot.<String>get("type")), "%" + getType().toLowerCase() + "%");
            predicateList.add(type);
        }
        return predicateList;
    }

}
