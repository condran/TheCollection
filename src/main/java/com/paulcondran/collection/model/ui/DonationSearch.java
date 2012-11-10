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
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 21/10/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DonationSearch {

    private String memberID;
    private String name;
    private String receipt;
    private Date dateFrom;
    private Date dateTo;
    private String ddt;
    private String collector;
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

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
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

    public String getDdt() {
        return ddt;
    }

    public void setDdt(String ddt) {
        this.ddt = ddt;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }


    public List<Predicate> listPredicates(Root<Donation> donationRoot, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(getMemberID())) {
            Predicate memberID = builder.like(
                    builder.lower(donationRoot.<String>get("memberID")), "%" + getMemberID().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getName())) {
            Predicate memberID = builder.like(builder.lower(donationRoot.<String>get("name")), "%" + getName().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getReceipt())) {
            Predicate memberID = builder.like(
                    builder.lower(donationRoot.<String>get("receiptNo")), "%" + getReceipt().toLowerCase() + "%");
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getDdt())) {
            Predicate memberID = builder.like(
                    builder.lower(donationRoot.<String>get("directDebitRef")), "%" + getDdt().toLowerCase() + "%");
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
