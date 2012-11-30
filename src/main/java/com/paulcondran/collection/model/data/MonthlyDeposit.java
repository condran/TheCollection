package com.paulcondran.collection.model.data;

import com.paulcondran.collection.DonationStatus;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Donation database entity model.
 */
@Entity
@Table(name = "MonthlyDeposit")
public class MonthlyDeposit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String organisation;

    private String collectorId;

    private String depositPeriod;

    private BigDecimal total;
    
    private BigDecimal extra;

    private BigDecimal less;
    
    @Enumerated(EnumType.STRING) 
    private DonationStatus donationStatus = DonationStatus.Initial;

    
    @OneToMany(mappedBy = "monthlyDeposit", cascade = {CascadeType.ALL})  
    private List<DepositItem> depositList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDepositPeriod() {
        return depositPeriod;
    }

    public void setDepositPeriod(String date) {
        this.depositPeriod = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the collectorId
     */
    public String getCollectorId() {
        return collectorId;
    }

    /**
     * @param collectorId the collectorId to set
     */
    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    /**
     * @return the extra
     */
    public BigDecimal getExtra() {
        return extra;
    }

    /**
     * @param extra the extra to set
     */
    public void setExtra(BigDecimal extra) {
        this.extra = extra;
    }

    /**
     * @return the less
     */
    public BigDecimal getLess() {
        return less;
    }

    /**
     * @param less the less to set
     */
    public void setLess(BigDecimal less) {
        this.less = less;
    }

    /**
     * @return the depositList
     */
    public List<DepositItem> getDepositList() {
        if (depositList == null) {
            depositList = new ArrayList<DepositItem>();
        }
        return depositList;
    }

    /**
     * @param depositList the depositList to set
     */
    public void setDepositList(List<DepositItem> depositList) {
        this.depositList = depositList;
    }

    /**
     * @return the organisation
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation the organisation to set
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
     * @return the donationStatus
     */
    public DonationStatus getDonationStatus() {
        return donationStatus;
    }

    /**
     * @param donationStatus the donationStatus to set
     */
    public void setDonationStatus(DonationStatus donationStatus) {
        this.donationStatus = donationStatus;
    }

}
