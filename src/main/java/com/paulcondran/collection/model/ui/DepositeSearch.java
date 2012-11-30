package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.DonationStatus;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.MonthlyDeposit;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.EnumUtils;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 21/10/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DepositeSearch {

    private String depositStatus;
    private String month;
    private String collector;
    private String organisation;
    private boolean unclosed;


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


    public List<Predicate> listPredicates(Root<MonthlyDeposit> depositeRoot, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<Predicate>();

        if (unclosed) {
            Predicate depStattus = builder.notEqual(builder.lower(depositeRoot.<String>get("donationStatus")), DonationStatus.Closed);
            predicateList.add(depStattus);
            depStattus = builder.notEqual(builder.lower(depositeRoot.<String>get("donationStatus")), DonationStatus.AdjustedPostClosing);
            predicateList.add(depStattus);
            
        }
        if (StringUtils.isNotBlank(getDepositStatus())) {
            DonationStatus dstatus = EnumUtils.getEnum(DonationStatus.class, getDepositStatus());
            Predicate depStattus = builder.equal(
                    builder.lower(depositeRoot.<String>get("donationStatus")), dstatus );
            predicateList.add(depStattus);
        }

        if (StringUtils.isNotBlank(getMonth())) {
            Predicate memberID = builder.greaterThanOrEqualTo(builder.lower(depositeRoot.<String>get("depositPeriod")),  getMonth().toLowerCase());
            predicateList.add(memberID);
        }

        if (StringUtils.isNotBlank(getOrganisation())) {
            Predicate memberID = builder.equal(builder.lower(depositeRoot.<String>get("organisation")),  getOrganisation().toLowerCase());
            predicateList.add(memberID);
        }
        if (StringUtils.isNotBlank(getCollector())) {
            Predicate memberID = builder.equal(builder.lower(depositeRoot.<String>get("collectorId")),  getCollector().toLowerCase());
            predicateList.add(memberID);
        }
        return predicateList;
    }

    /**
     * @return the depositStatus
     */
    public String getDepositStatus() {
        return depositStatus;
    }

    /**
     * @return the Month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param depositStatus the depositStatus to set
     */
    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    /**
     * @param Month the Month to set
     */
    public void setMonth(String Month) {
        this.month = Month;
    }

    /**
     * @return the unclosed
     */
    public boolean isUnclosed() {
        return unclosed;
    }

    /**
     * @param unclosed the unclosed to set
     */
    public void setUnclosed(boolean unclosed) {
        this.unclosed = unclosed;
    }
}
