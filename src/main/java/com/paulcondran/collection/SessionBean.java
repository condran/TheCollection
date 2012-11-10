package com.paulcondran.collection;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

/**
 * Custom Session object to hold application specific data.
 */
public class SessionBean extends AuthenticatedWebSession {

    private Roles roles = new Roles();
    private String activePageName = "";
    private String userName = "";


    /**
     * Constructor. Note that {@link org.apache.wicket.request.cycle.RequestCycle} is not available until this constructor returns.
     *
     * @param request The current request
     */
    public SessionBean(Request request) {
        super(request);
    }

    /**
     * Called after authenticate, provide the user roles
     * @return   return the user roles
     */
    @Override
    public Roles getRoles() {
        return roles;
    }

    /**
     * Authenticate the user
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean authenticate(String username, String password) {
        if (UIConstants.ADMIN_ROLE.equalsIgnoreCase(username)) {
            roles.add(UIConstants.ADMIN_ROLE);
        }
        roles.add(UIConstants.USER_ROLE);

        this.userName = username;

        return true;
    }

    /**
     * The active page name
     * @return
     */
    public String getActivePageName() {
        return activePageName;
    }

    public void setActivePageName(String activePageName) {
        this.activePageName = activePageName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
