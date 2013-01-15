package com.paulcondran.collection.model.data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User database entity model.
 */
@Entity
@Table(name = "AppUsers")
public class User implements Serializable {

    @Id
    private String userID;
    
    private String name;
    
    private String type;
    
    private String mobileNo;
    
    private String emailAddress;
    
    private String password;

    private String organisation;
    
    private String collectorCode;

    public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the organisation
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation the organisation to set
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
     * @return the collectorCode
     */
    public String getCollectorCode() {
        return collectorCode;
    }

    /**
     * @param collectorCode the collectorCode to set
     */
    public void setCollectorCode(String collectorCode) {
        this.collectorCode = collectorCode;
    }

}
