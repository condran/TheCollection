package com.tekinsure.thecollection.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base Page
 * This class contains common logic for all pages in the application.
 *
 * @author Paul Condran
 */
public class BasePage extends WebPage {

    public BasePage() {

    }

    public BasePage(PageParameters parameters) {
        super(parameters);
    }


}
