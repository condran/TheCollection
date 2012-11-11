package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.Promise;
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
public class PromiseSearch {

    private String memberID;
    private String name;
    private String finYear;
    private String ddt;
    private String organisation;

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

     public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String dateFrom) {
        this.finYear = dateFrom;
    }

    public String getDdt() {
        return ddt;
    }

    public void setDdt(String ddt) {
        this.ddt = ddt;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }
    
        public List<Predicate> listPredicates(Root<Promise> promiseRoot, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(getMemberID())) {
            Predicate memberID = builder.like(
                    builder.lower(promiseRoot.<String>get("memberID")), "%" + getMemberID().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getName())) {
            Predicate memberID = builder.like(builder.lower(promiseRoot.<String>get("name")), "%" + getName().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getFinYear())) {
            Predicate memberID = builder.equal(
                    builder.lower(promiseRoot.<String>get("financialYear")), getFinYear());
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getDdt())) {
            Predicate memberID = builder.like(
                    builder.lower(promiseRoot.<String>get("directDebitRef")), "%" + getDdt().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getOrganisation())) {
            Predicate memberID = builder.like(
                    builder.lower(promiseRoot.<String>get("organisation")), "%" + getDdt().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        return predicateList;
    }

}
