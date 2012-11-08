package com.paulcondran.collection.model.ui;

/**
 * A simple model to hold dropdown code / values
 */
public class OptionItem {

    private String code;

    private String description;

    public OptionItem() {
    }

    public OptionItem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
