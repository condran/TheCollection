package com.paulcondran.collection.components;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Bootstrap version of feedback panel
 */
public class BootstrapFeedbackPanel extends FeedbackPanel {

    private static Map levelClassMapping = new HashMap() {{
        put(FeedbackMessage.DEBUG, "");
        put(FeedbackMessage.WARNING, "");
        put(FeedbackMessage.INFO, "alert-info");
        put(FeedbackMessage.SUCCESS, "alert-success");
        put(FeedbackMessage.ERROR, "alert-error");
        put(FeedbackMessage.FATAL, "alert-error");
    }};

    /**
     * @see org.apache.wicket.Component#Component(String)
     */
    public BootstrapFeedbackPanel(String id) {
        super(id);
    }

    /**
     * Gets the css class for the given message.
     *
     * @param message the message
     * @return the css class; by default, this returns feedbackPanel + the message level, eg
     *         'feedbackPanelERROR', but you can override this method to provide your own
     */
    @Override
    protected String getCSSClass(FeedbackMessage message) {
        return "alert " + levelClassMapping.get(message.getLevel());
    }
}
