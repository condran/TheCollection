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
@Table(name = "Donation")
public class Donation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String memberID;

    private String name;

    private String receiptNo;

    private String directDebitRef;

    private Boolean isDirectDebit = false;

    private String collector;

    private String orgChapter;

    private Date date;

    private BigDecimal total;
    
    private String details;
    
    @Enumerated(EnumType.STRING) 
    private DonationStatus donationStatus = DonationStatus.Initial;

//    @ElementCollection
    @OneToMany(mappedBy = "donation", cascade = {CascadeType.ALL})  
    private List<DonationCategory> categoryList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDirectDebitRef() {
        return directDebitRef;
    }

    public void setDirectDebitRef(String directDebitRef) {
        this.directDebitRef = directDebitRef;
    }

    public Boolean getDirectDebit() {
        return isDirectDebit;
    }

    public void setDirectDebit(boolean directDebit) {
        isDirectDebit = directDebit;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getOrgChapter() {
        return orgChapter;
    }

    public void setOrgChapter(String orgChapter) {
        this.orgChapter = orgChapter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DonationCategory> getCategoryList() {
        if (categoryList == null) {
            categoryList = new ArrayList<DonationCategory>();
        }
        return categoryList;
    }

    public void setCategoryList(List<DonationCategory> donationCategoryList) {
        this.categoryList = donationCategoryList;
    }

    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
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
