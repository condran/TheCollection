package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.Member;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 21/10/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemberSearch {

    private String memberID;
    private String name;
    private Date dateFrom;
    private Date dateTo;
    private String suburb;
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }
    
        public List<Predicate> listPredicates(Root<Member> memberRoot, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(getMemberID())) {
            Predicate memberID = builder.like(
                    builder.lower(memberRoot.<String>get("memberID")), "%" + getMemberID().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getName())) {
            Predicate memberID = builder.like(builder.lower(memberRoot.<String>get("name")), "%" + getName().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (getDateFrom() != null) {
            Predicate memberID = builder.greaterThanOrEqualTo(memberRoot.<Date>get("dateFrom"), getDateFrom());
            predicateList.add(memberID);
        }

        if (getDateTo() != null) {
            Predicate memberID = builder.lessThanOrEqualTo(memberRoot.<Date>get("dateTo"), getDateTo());
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getOrganisation())) {
            Predicate memberID = builder.like(
                    builder.lower(memberRoot.<String>get("organisation")), "%" + getOrganisation().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getSuburb())) {
            Predicate memberID = builder.like(
                    builder.lower(memberRoot.<String>get("suburb")), "%" + getSuburb().toLowerCase() + "%");
            predicateList.add(memberID);
        }
        return predicateList;
    }
}
