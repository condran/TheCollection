package com.paulcondran.collection;

import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.model.data.User;
import org.apache.commons.lang3.StringUtils;
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
    private String organisation = "";
    private String CollectorCode = "";


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
        if (UIConstants.ROLE_ADMIN.equalsIgnoreCase(username)) {
            roles.add(UIConstants.ROLE_ADMIN);
            roles.add(UIConstants.ROLE_USER);
            return true;
        } else {
            if (StringUtils.isBlank(password)) {return false; }
            User user = CollectionUtil.loadUser(username);
            if (password.equals(user.getPassword())) {
                organisation = user.getOrganisation();
                CollectorCode = user.getCollectorCode();
                roles.add(user.getType());
                if (!UIConstants.ROLE_USER.equalsIgnoreCase(user.getType())) {
                    roles.add(UIConstants.ROLE_USER);
                }
                this.userName = username;
                return true;
            }
        }
        return false;
    }

    public boolean switchUser(User user) {
        roles.clear();        
        organisation = "";
        CollectorCode = "";
        organisation = user.getOrganisation();
        CollectorCode = user.getCollectorCode();
        roles.add(user.getType());
        if (!UIConstants.ROLE_USER.equalsIgnoreCase(user.getType())) {
            roles.add(UIConstants.ROLE_USER);
        }
        this.userName = user.getUserID();
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
     * @return the CollectorCode
     */
    public String getCollectorCode() {
        return CollectorCode;
    }

    /**
     * @param CollectorCode the CollectorCode to set
     */
    public void setCollectorCode(String CollectorCode) {
        this.CollectorCode = CollectorCode;
    }
}
