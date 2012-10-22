package com.tekinsure.thecollection.model.data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Category implements Serializable {

    @Id
    private String categoryName;

    private BigDecimal amount;

}
