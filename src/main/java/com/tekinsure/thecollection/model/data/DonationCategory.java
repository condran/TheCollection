package com.tekinsure.thecollection.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DonationCategory database entity model.
 */
@Entity
@Table(name = "DonationCategory")
public class DonationCategory implements Serializable {

    @Id
    private String categoryName;

    private BigDecimal amount;

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
}
