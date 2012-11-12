package com.paulcondran.collection.model.data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DonationCategory database entity model.
 */
@Entity
@Table(name = "PromiseCategory")
public class PromiseCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String categoryName;

    @ManyToOne(fetch= FetchType.EAGER)
    private Promise promise;

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
    public Promise getPromise() {
        return promise;
    }

    /**
     * @param donation the donation to set
     */
    public void setPromise(Promise promise) {
        this.promise = promise;
    }
}
