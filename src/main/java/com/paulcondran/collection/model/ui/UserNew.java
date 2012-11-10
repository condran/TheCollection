package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.User;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserNew {

    private User user;
    private String password;
    private String passverify;

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
     * @return the passverify
     */
    public String getPassverify() {
        return passverify;
    }

    /**
     * @param passverify the passverify to set
     */
    public void setPassverify(String passverify) {
        this.passverify = passverify;
    }
}
