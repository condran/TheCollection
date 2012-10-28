package com.tekinsure.thecollection.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

/**
 * DonationCategory database entity model.
 */
@Entity
@Table(name = "DonationCategory")
public class DonationCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String categoryName;

    @ManyToOne(fetch= FetchType.EAGER)
    private Donation donation;

    private BigDecimal amount;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the donation
     */
    public Donation getDonation() {
        return donation;
    }

    /**
     * @param donation the donation to set
     */
    public void setDonation(Donation donation) {
        this.donation = donation;
    }
}
