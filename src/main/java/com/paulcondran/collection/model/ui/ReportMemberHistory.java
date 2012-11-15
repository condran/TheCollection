package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.Donation;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Java bean to store the report form data
 *
 */
public class ReportMemberHistory {

    private String memberSearch;

    private String memberID;

    private Date dateFrom;

    private Date dateTo;

    public String getMemberSearch() {
        return memberSearch;
    }

    public void setMemberSearch(String memberSearch) {
        this.memberSearch = memberSearch;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
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

    public List<Predicate> listPredicates(Root<Donation> donationRoot, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(getMemberID())) {
            Predicate memberID = builder.like(
                    builder.lower(donationRoot.<String>get("memberID")), "%" + getMemberID().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (getDateFrom() != null) {
            Predicate memberID = builder.greaterThanOrEqualTo(donationRoot.<Date>get("date"), getDateFrom());
            predicateList.add(memberID);
        }

        if (getDateTo() != null) {
            Predicate memberID = builder.lessThanOrEqualTo(donationRoot.<Date>get("date"), getDateTo());
            predicateList.add(memberID);
        }

        return predicateList;
    }
}
