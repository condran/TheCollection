package com.paulcondran.collection.model.ui;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 21/10/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class PromiseSearch {

    private String memberID;
    private String name;
    private String finYear;
    private String ddt;
    private String organisation;

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

     public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String dateFrom) {
        this.finYear = dateFrom;
    }

    public String getDdt() {
        return ddt;
    }

    public void setDdt(String ddt) {
        this.ddt = ddt;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }
}
