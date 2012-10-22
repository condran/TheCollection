package com.tekinsure.thecollection.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Member implements Serializable {

    @Id
    private String memberID;

    private String directDebitRef;

    private String name;

    private Date startDate;

    private Date endDate;

    private String emailAddress;

    private String mobileNo;

    private String contactNo;

    private String orgChapter;

    private Integer yearOfBirth;

    private Date dateOfBirth;

    private String address1;

    private String address2;

    private String suburb;

    private String state;

    private String postcode;

}
