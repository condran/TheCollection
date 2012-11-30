package com.paulcondran.collection.model.data;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User database entity model.
 */
@Entity
public class AppConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private String key1;
    private String key2;
    private String key3;
    private String Value;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the key1
     */
    public String getKey1() {
        return key1;
    }

    /**
     * @param key1 the key1 to set
     */
    public void setKey1(String key1) {
        this.key1 = key1;
    }

    /**
     * @return the key2
     */
    public String getKey2() {
        return key2;
    }

    /**
     * @param key2 the key2 to set
     */
    public void setKey2(String key2) {
        this.key2 = key2;
    }

    /**
     * @return the key3
     */
    public String getKey3() {
        return key3;
    }

    /**
     * @param key3 the key3 to set
     */
    public void setKey3(String key3) {
        this.key3 = key3;
    }

    /**
     * @return the Value
     */
    public String getValue() {
        return Value;
    }

    /**
     * @param Value the Value to set
     */
    public void setValue(String Value) {
        this.Value = Value;
    }
    
    

}
