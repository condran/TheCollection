package com.tekinsure.thecollection.model.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Donation implements Serializable {

    @Id
    private Long id;

    private String memberId;

    private String name;

    private String receiptNo;

    private String directDebitRef;

    private boolean isDirectDebit;

    private String collector;

    private String orgChapter;

    private Date date;

    private BigDecimal total;

    private List<Category> categoryList;
}
