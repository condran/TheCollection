package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.DonationStatus;
import com.paulcondran.collection.model.data.*;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DepositNew {

    private MonthlyDeposit deposit;
    
    private Date date1;
    private Date date2;
    private Date date3;
    private Date date4;
    private Date date5;
    private Date date6;
    
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal amount3;
    private BigDecimal amount4;
    private BigDecimal amount5;
    private BigDecimal amount6;

    public DepositNew() {
        deposit = new MonthlyDeposit();
        deposit.setDonationStatus(DonationStatus.Initial);
    }
    /**
     * @return the deposit
     */
    public MonthlyDeposit getDeposit() {
        if (deposit == null) {
            return new MonthlyDeposit();
        }
        return deposit;
    }

    /**
     * @param deposit the deposit to set
     */
    public void setDeposit(MonthlyDeposit deposit) {
        this.deposit = deposit;
    }

    /**
     * @return the date1
     */
    public Date getDate1() {
        return date1;
    }

    /**
     * @param date1 the date1 to set
     */
    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    /**
     * @return the date2
     */
    public Date getDate2() {
        return date2;
    }

    /**
     * @param date2 the date2 to set
     */
    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    /**
     * @return the date3
     */
    public Date getDate3() {
        return date3;
    }

    /**
     * @param date3 the date3 to set
     */
    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    /**
     * @return the date4
     */
    public Date getDate4() {
        return date4;
    }

    /**
     * @param date4 the date4 to set
     */
    public void setDate4(Date date4) {
        this.date4 = date4;
    }

    /**
     * @return the date5
     */
    public Date getDate5() {
        return date5;
    }

    /**
     * @param date5 the date5 to set
     */
    public void setDate5(Date date5) {
        this.date5 = date5;
    }

    /**
     * @return the date6
     */
    public Date getDate6() {
        return date6;
    }

    /**
     * @param date6 the date6 to set
     */
    public void setDate6(Date date6) {
        this.date6 = date6;
    }

    /**
     * @return the amount1
     */
    public BigDecimal getAmount1() {
        return amount1;
    }

    /**
     * @param amount1 the amount1 to set
     */
    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    /**
     * @return the amount2
     */
    public BigDecimal getAmount2() {
        return amount2;
    }

    /**
     * @param amount2 the amount2 to set
     */
    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    /**
     * @return the amount3
     */
    public BigDecimal getAmount3() {
        return amount3;
    }

    /**
     * @param amount3 the amount3 to set
     */
    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

    /**
     * @return the amount4
     */
    public BigDecimal getAmount4() {
        return amount4;
    }

    /**
     * @param amount4 the amount4 to set
     */
    public void setAmount4(BigDecimal amount4) {
        this.amount4 = amount4;
    }

    /**
     * @return the amount5
     */
    public BigDecimal getAmount5() {
        return amount5;
    }

    /**
     * @param amount5 the amount5 to set
     */
    public void setAmount5(BigDecimal amount5) {
        this.amount5 = amount5;
    }

    /**
     * @return the amount6
     */
    public BigDecimal getAmount6() {
        return amount6;
    }

    /**
     * @param amount6 the amount6 to set
     */
    public void setAmount6(BigDecimal amount6) {
        this.amount6 = amount6;
    }

}
