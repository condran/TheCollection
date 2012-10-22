package com.tekinsure.thecollection;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessionBean extends WebSession {

    /**
     * Constructor. Note that {@link org.apache.wicket.request.cycle.RequestCycle} is not available until this constructor returns.
     *
     * @param request The current request
     */
    public SessionBean(Request request) {
        super(request);
    }


}
