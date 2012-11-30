package com.paulcondran.collection.model.data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * DonationCategory database entity model.
 */
@Entity
@Table(name = "DepositItem")
public class DepositItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch= FetchType.EAGER)
    private MonthlyDeposit monthlyDeposit;

    private Date date;
    private BigDecimal amount;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the monthlyDeposit
     */
    public MonthlyDeposit getMonthlyDeposit() {
        return monthlyDeposit;
    }

    /**
     * @param monthlyDeposit the monthlyDeposit to set
     */
    public void setMonthlyDeposit(MonthlyDeposit monthlyDeposit) {
        this.monthlyDeposit = monthlyDeposit;
    }

}
