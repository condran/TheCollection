package com.tekinsure.thecollection.model.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

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

    private boolean isDirectDebit;

    private String collector;

    private String orgChapter;

    private Date date;

    private BigDecimal total;

    @ElementCollection
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

    public boolean isDirectDebit() {
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
}
