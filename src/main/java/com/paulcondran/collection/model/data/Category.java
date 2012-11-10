package com.paulcondran.collection.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * This entity contains the list of categories that are being run.
 */
@Entity
public class Category implements Serializable {

    @Id
    private String code;

    private String categoryName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
