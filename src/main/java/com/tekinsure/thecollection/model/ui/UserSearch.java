package com.tekinsure.thecollection.model.ui;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 21/10/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserSearch {

    private String userID;
    private String name;
    private String type;

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserID() {
        return userID;
    }

    public void setUserID(String memberID) {
        this.userID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
