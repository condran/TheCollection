package com.paulcondran.collection.model.ui;

import java.math.BigDecimal;

/**
 * Category report item for rolling out categories into table structures
 */
public class CategoryReportItem {

    private String categoryCode;
    private String categoryName;
    private BigDecimal categoryTotal;

    public CategoryReportItem() {
    }

    public CategoryReportItem(String categoryCode, String categoryName, BigDecimal categoryTotal) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.categoryTotal = categoryTotal;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getCategoryTotal() {
        if (categoryTotal == null) {
            categoryTotal = new BigDecimal(0);
        }
        return categoryTotal;
    }

    public void setCategoryTotal(BigDecimal categoryTotal) {
        this.categoryTotal = categoryTotal;
    }

    public void addTotal(BigDecimal amount) {
        setCategoryTotal(getCategoryTotal().add(amount));
    }
}
