package com.paulcondran.collection.components;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * A behaviour for auto-complete in Bootstrap based sites
 *
 * @author Paul Condran
 */
public abstract class BootstrapTypeAheadBehaviour extends AbstractAjaxBehavior {

    @Override
    public void onRequest() {
        RequestCycle requestCycle = RequestCycle.get();
        WebRequest webRequest = (WebRequest)requestCycle.getRequest();
        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getContainerRequest();

        String searchQuery = httpRequest.getParameter("query");
        List<String> choices = getChoices(searchQuery);

        JSONArray jsonArray = new JSONArray(choices);
        requestCycle.scheduleRequestHandlerAfterCurrent(new TextRequestHandler("application/json",
                        "UTF-8", jsonArray.toString()));
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String callbackURL =  getCallbackUrl().toString();
        tag.put("data-link", callbackURL);
        tag.put("data-provide", "typeahead");
    }

    /**
     * This method is called when the user types a query
     * @param search the string input typed by the user
     * @return       a list of string matches
     */
    public abstract List<String> getChoices(String search);

}
