package com.paulcondran.collection.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * User database entity model.
 */
@Entity
public class CategoryDef implements Serializable {

    @Id
    private String categoryID;
    
    private String name;
    
    private boolean promiseCategory;
    
    private boolean belongsToGroup1;

    private boolean belongsToGroup2;

    /**
     * @return the categoryID
     */
    public String getCategoryID() {
        return categoryID;
    }

    /**
     * @param categoryID the categoryID to set
     */
    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the promiseCategory
     */
    public boolean isPromiseCategory() {
        return promiseCategory;
    }

    /**
     * @param promiseCategory the promiseCategory to set
     */
    public void setPromiseCategory(boolean promiseCategory) {
        this.promiseCategory = promiseCategory;
    }

    /**
     * @return the promiseCategory
     */
    public boolean getIsPromiseCategory() {
        return promiseCategory;
    }

    /**
     * @param promiseCategory the promiseCategory to set
     */
    public void setIsPromiseCategory(boolean promiseCategory) {
        this.promiseCategory = promiseCategory;
    }

    /**
     * @return the belongsToGroup1
     */
    public boolean isBelongsToGroup1() {
        return belongsToGroup1;
    }

    /**
     * @param belongsToGroup1 the belongsToGroup1 to set
     */
    public void setBelongsToGroup1(boolean belongsToGroup1) {
        this.belongsToGroup1 = belongsToGroup1;
    }

    /**
     * @return the belongsToGroup2
     */
    public boolean isBelongsToGroup2() {
        return belongsToGroup2;
    }

    /**
     * @param belongsToGroup2 the belongsToGroup2 to set
     */
    public void setBelongsToGroup2(boolean belongsToGroup2) {
        this.belongsToGroup2 = belongsToGroup2;
    }


}
