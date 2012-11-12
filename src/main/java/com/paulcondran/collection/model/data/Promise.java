package com.paulcondran.collection.model.data;

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
@Table(name = "Promise")
public class Promise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String memberID;

    private String name;

    private String directDebitRef;

    private String financialYear;
    
    private String organisation;

    private BigDecimal total;
    
    private String details;

//    @ElementCollection
    @OneToMany(mappedBy = "promise", cascade = {CascadeType.ALL})  
    private List<PromiseCategory> categoryList;

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

    public String getDirectDebitRef() {
        return directDebitRef;
    }

    public void setDirectDebitRef(String directDebitRef) {
        this.directDebitRef = directDebitRef;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<PromiseCategory> getCategoryList() {
        if (categoryList == null) {
            categoryList = new ArrayList<PromiseCategory>();
        }
        return categoryList;
    }

    public void setCategoryList(List<PromiseCategory> donationCategoryList) {
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
     * @return the financialYear
     */
    public String getFinancialYear() {
        return financialYear;
    }

    /**
     * @param financialYear the financialYear to set
     */
    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
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
}
