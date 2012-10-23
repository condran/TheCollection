package com.tekinsure.thecollection.model.ui;

import com.tekinsure.thecollection.model.data.Donation;
import com.tekinsure.thecollection.model.data.User;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserNew {

    private User user;

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setMember(User user) {
        this.user = user;
    }
}
